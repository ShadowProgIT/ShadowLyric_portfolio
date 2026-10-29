package com.logistic.publisher;

import com.logistic.events.EventEnvelope;
import com.logistic.events.RoutingKeys;
import com.logistic.events.SendingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EnrichmentEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EnrichmentEventPublisher.class);
    private final RabbitTemplate rabbitTemplate;

    public EnrichmentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEnriched(SendingEvent.Enriched enrichedEvent) {
        EventEnvelope<SendingEvent.Enriched> envelope = EventEnvelope.wrap(
                enrichedEvent,
                "logistics-enrichment-client",
                RoutingKeys.SENDING_ENRICHED
        );
        rabbitTemplate.convertAndSend(RoutingKeys.EXCHANGE, RoutingKeys.SENDING_ENRICHED, envelope);
        log.info("Опубликовано событие sending.enriched для отправки id={}", enrichedEvent.id());
    }
}
