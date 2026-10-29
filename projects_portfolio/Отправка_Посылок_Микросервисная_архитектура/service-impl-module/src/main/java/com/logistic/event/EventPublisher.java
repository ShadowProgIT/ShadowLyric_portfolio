package com.logistic.event;

import com.logistic.events.EventEnvelope;
import com.logistic.events.RoutingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {
    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);
    private final RabbitTemplate rabbitTemplate;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(Object payload, String eventType) {
        try {
            var envelope = EventEnvelope.wrap(payload, "logistic-service", eventType);
            rabbitTemplate.convertAndSend(RoutingKeys.EXCHANGE, eventType, envelope);
            log.info("Событие опубликовано: {} [eventId={}]", eventType, envelope.metadata().eventId());
        } catch (Exception e) {
            log.error("НЕ УДАЛОСЬ опубликовать событие {}: {}", eventType, e.getMessage(), e);
        }
    }
}
