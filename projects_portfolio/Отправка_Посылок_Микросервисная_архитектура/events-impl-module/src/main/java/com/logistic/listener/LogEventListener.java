package com.logistic.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistic.events.EntrantEvent;
import com.logistic.events.EventMetadata;
import com.logistic.events.SendingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;

@Component
public class LogEventListener {

    private static final Logger log = LoggerFactory.getLogger(LogEventListener.class);
    private static final String AUDIT_FILE_NAME = "sending_audit.log";
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_INSTANT;

    private final ObjectMapper jsonMapper;

    public LogEventListener(ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @RabbitListener(queues = "q.logistic.audit.events", containerFactory = "rabbitListenerContainerFactory")
    public void handleEvent(Message message) {
        try {
            byte[] body = message.getBody();
            JsonNode root = jsonMapper.readTree(body);

            JsonNode metaNode = root.get("metadata");
            EventMetadata metadata = jsonMapper.treeToValue(metaNode, EventMetadata.class);

            JsonNode payloadNode = root.get("payload");
            String eventType = metadata.eventType();

            String auditLine = buildAuditLine(eventType, payloadNode, metadata);

            synchronized (LogEventListener.class) {
                Path path = Paths.get(AUDIT_FILE_NAME);
                try (BufferedWriter writer = Files.newBufferedWriter(path,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND)) {
                    writer.write(auditLine);
                    writer.newLine();
                }
            }

            log.info("Событие {} записано в файл {}", eventType, AUDIT_FILE_NAME);

        } catch (Exception e) {
            log.error("Ошибка обработки события для файлового аудита: {}", e.getMessage(), e);
        }
    }

    private String buildAuditLine(String eventType, JsonNode payload, EventMetadata metadata) throws Exception {
        String timestamp = ISO_FORMATTER.format(metadata.timestamp());
        String source = metadata.serviceName();
        String eventId = metadata.eventId();

        return switch (eventType) {
            case "sending.created" -> {
                SendingEvent.Created e = jsonMapper.treeToValue(payload, SendingEvent.Created.class);
                yield String.format("[%s] %s | eventId=%s | Создана отправка: id=%d, трекинг=%s, вес=%s кг, отправитель=%d, получатель=%d",
                        timestamp, eventType, eventId, e.id(), e.trackingNumber(), e.weight(), e.senderId(), e.recipientId());
            }
            case "sending.status.updated" -> {
                SendingEvent.Update e = jsonMapper.treeToValue(payload, SendingEvent.Update.class);
                yield String.format("[%s] %s | eventId=%s | Обновление статуса: трекинг=%s, %s -> %s",
                        timestamp, eventType, eventId, e.trackingNumber(), e.oldStatus(), e.newStatus());
            }
            case "sending.deleted" -> {
                SendingEvent.Deleted e = jsonMapper.treeToValue(payload, SendingEvent.Deleted.class);
                yield String.format("[%s] %s | eventId=%s | Удалена отправка: id=%d, трекинг=%s",
                        timestamp, eventType, eventId, e.id(), e.trackingNumber());
            }
            case "sending.enriched" -> {
                SendingEvent.Enriched e = jsonMapper.treeToValue(payload, SendingEvent.Enriched.class);
                yield String.format("[%s] %s | eventId=%s | Обогащение: отправка id=%d, трекинг=%s, ETA=%d мин, статус маршрута=%s",
                        timestamp, eventType, eventId, e.id(), e.trackingNumber(), e.estimatedMinutes(), e.routeStatus());
            }
            case "entrant.created" -> {
                EntrantEvent.Created e = jsonMapper.treeToValue(payload, EntrantEvent.Created.class);
                yield String.format("[%s] %s | eventId=%s | Создан участник: id=%d, %s %s, email=%s",
                        timestamp, eventType, eventId, e.entrantId(), e.firstname(), e.lastname(), e.email());
            }
            case "entrant.deleted" -> {
                EntrantEvent.Deleted e = jsonMapper.treeToValue(payload, EntrantEvent.Deleted.class);
                yield String.format("[%s] %s | eventId=%s | Удалён участник: id=%d, %s %s, тел=%s",
                        timestamp, eventType, eventId, e.entrantId(), e.firstname(), e.lastname(), e.phone());
            }
            default -> String.format("[%s] %s | eventId=%s | Неизвестный тип события: %s, источник=%s",
                    timestamp, eventType, eventId, eventType, source);
        };
    }
}