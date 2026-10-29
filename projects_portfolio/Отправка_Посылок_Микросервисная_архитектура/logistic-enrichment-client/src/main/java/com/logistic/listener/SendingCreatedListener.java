package com.logistic.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistic.events.SendingEvent;
import com.logistic.grpc.contract.ETACalculationRequest;
import com.logistic.grpc.contract.ETACalculationResponse;
import com.logistic.grpc.contract.ShipmentCalculatorGrpc;
import com.logistic.publisher.EnrichmentEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SendingCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(SendingCreatedListener.class);

    private final ShipmentCalculatorGrpc.ShipmentCalculatorBlockingStub calculatorStub;
    private final EnrichmentEventPublisher enrichmentPublisher;
    private final ObjectMapper jsonMapper;

    public SendingCreatedListener(ShipmentCalculatorGrpc.ShipmentCalculatorBlockingStub calculatorStub,
                                  EnrichmentEventPublisher enrichmentPublisher,
                                  ObjectMapper jsonMapper) {
        this.calculatorStub = calculatorStub;
        this.enrichmentPublisher = enrichmentPublisher;
        this.jsonMapper = jsonMapper;
    }

    @RabbitListener(queues = "q.logistics.enrichment.sending-created")
    public void handleSendingCreated(Message message) {
        try {
            JsonNode root = jsonMapper.readTree(message.getBody());
            JsonNode payloadNode = root.get("payload");
            SendingEvent.Created createdEvent = jsonMapper.treeToValue(payloadNode, SendingEvent.Created.class);

            log.info("📥 Получено событие sending.created для отправки id={}", createdEvent.id());

            ETACalculationRequest grpcRequest = ETACalculationRequest.newBuilder()
                    .setSendingId(createdEvent.id())
                    .setOriginCity("Москва")
                    .setDestCity("Санкт-Петербург")
                    .setWeightKg(createdEvent.weight() != null ? Double.parseDouble(createdEvent.weight()) : 1.0)
                    .build();

            log.info("Вызов gRPC: ShipmentCalculator.CalculateETA для отправки {}", createdEvent.id());

            ETACalculationResponse grpcResponse = calculatorStub.calculateETA(grpcRequest);

            log.info("gRPC ответ получен: отправка id={}, ETA={} мин, статус={}",
                    grpcResponse.getSendingId(), grpcResponse.getEstimatedMinutes(), grpcResponse.getRouteStatus());

            SendingEvent.Enriched enrichedEvent = new SendingEvent.Enriched(
                    createdEvent.id(),
                    createdEvent.trackingNumber(),
                    grpcResponse.getEstimatedMinutes(),
                    grpcResponse.getRouteStatus()
            );

            enrichmentPublisher.publishEnriched(enrichedEvent);

        } catch (Exception e) {
            log.error("Ошибка при обогащении отправки: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось обработать событие создания отправки", e);
        }
    }
}
