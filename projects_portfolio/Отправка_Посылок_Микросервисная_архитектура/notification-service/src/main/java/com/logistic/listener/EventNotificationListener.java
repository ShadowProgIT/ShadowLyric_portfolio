package com.logistic.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistic.events.EntrantEvent;
import com.logistic.events.EventMetadata;
import com.logistic.events.SendingEvent;
import com.logistic.websocket.NotificationWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.logistic.config.RabbitMQConfig.NOTIFICATIONS_QUEUE;

@Component
public class EventNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(EventNotificationListener.class);

    private final NotificationWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;
    private final Set<String> processedEventIds = ConcurrentHashMap.newKeySet();

    public EventNotificationListener(NotificationWebSocketHandler webSocketHandler,
                                     ObjectMapper objectMapper) {
        this.webSocketHandler = webSocketHandler;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = NOTIFICATIONS_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void handleEvent(Message message) {
        try {
            byte[] body = message.getBody();
            JsonNode root = objectMapper.readTree(body);

            JsonNode metaNode = root.get("metadata");
            EventMetadata metadata = objectMapper.treeToValue(metaNode, EventMetadata.class);

            if (!processedEventIds.add(metadata.eventId())) {
                log.debug("Дубликат события {} пропущен", metadata.eventId());
                return;
            }

            JsonNode payloadNode = root.get("payload");
            String eventType = metadata.eventType();

            String title = buildTitle(eventType);
            String description = buildDescription(eventType, payloadNode);
            String icon = resolveIcon(eventType);
            String level = resolveLevel(eventType);

            NotificationPayload payload = new NotificationPayload(
                    "NOTIFICATION",
                    metadata.eventId(),
                    eventType,
                    title,
                    description,
                    icon,
                    level,
                    metadata.serviceName(),
                    metadata.timestamp().toString(),
                    Instant.now().toString()
            );

            String json = objectMapper.writeValueAsString(payload);
            webSocketHandler.broadcast(json);

            log.info("[NOTIFY] {} | {} (клиентов: {})",
                    eventType, description, webSocketHandler.getActiveConnectionCount());

        } catch (Exception e) {
            log.error("Ошибка обработки события для уведомлений: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось обработать событие", e);
        }
    }

    private String buildTitle(String eventType) {
        return switch (eventType) {
            case "sending.created"        -> "Новая отправка";
            case "sending.status.updated" -> "Статус отправки изменён";
            case "sending.deleted"        -> "Отправка удалена";
            case "sending.enriched"       -> "Обогащение отправки";
            case "entrant.created"        -> "Новый участник";
            case "entrant.deleted"        -> "Участник удалён";
            default                       -> "Событие: " + eventType;
        };
    }

    private String buildDescription(String eventType, JsonNode payload) {
        try {
            return switch (eventType) {
                case "sending.created" -> {
                    SendingEvent.Created e = objectMapper.treeToValue(payload, SendingEvent.Created.class);
                    yield "Создана отправка №%s (вес: %s кг)".formatted(e.trackingNumber(), e.weight());
                }
                case "sending.status.updated" -> {
                    SendingEvent.Update e = objectMapper.treeToValue(payload, SendingEvent.Update.class);
                    yield "Отправка %s: %s → %s".formatted(e.trackingNumber(), e.oldStatus(), e.newStatus());
                }
                case "sending.deleted" -> {
                    SendingEvent.Deleted e = objectMapper.treeToValue(payload, SendingEvent.Deleted.class);
                    yield "Удалена отправка №%s".formatted(e.trackingNumber());
                }
                case "sending.enriched" -> {
                    SendingEvent.Enriched e = objectMapper.treeToValue(payload, SendingEvent.Enriched.class);
                    yield "ETA: %d мин, статус маршрута: %s".formatted(e.estimatedMinutes(), e.routeStatus());
                }
                case "entrant.created" -> {
                    EntrantEvent.Created e = objectMapper.treeToValue(payload, EntrantEvent.Created.class);
                    yield "Зарегистрирован участник: %s %s".formatted(e.firstname(), e.lastname());
                }
                case "entrant.deleted" -> {
                    EntrantEvent.Deleted e = objectMapper.treeToValue(payload, EntrantEvent.Deleted.class);
                    yield "Удалён участник: %s %s".formatted(e.firstname(), e.lastname());
                }
                default -> "Событие " + eventType;
            };
        } catch (Exception e) {
            return "Событие " + eventType + " (ошибка парсинга)";
        }
    }

    private String resolveIcon(String eventType) {
        return switch (eventType) {
            case "sending.created"        -> "package-plus";
            case "sending.status.updated" -> "package-edit";
            case "sending.deleted"        -> "package-remove";
            case "sending.enriched"       -> "analytics";
            case "entrant.created"        -> "user-plus";
            case "entrant.deleted"        -> "user-remove";
            default                       -> "bell";
        };
    }

    private String resolveLevel(String eventType) {
        return switch (eventType) {
            case "sending.deleted", "entrant.deleted" -> "warning";
            case "sending.enriched"                   -> "info";
            default                                   -> "success";
        };
    }

    private record NotificationPayload(
            String type,
            String eventId,
            String eventType,
            String title,
            String description,
            String icon,
            String level,
            String source,
            String eventTimestamp,
            String receivedAt
    ) {}
}