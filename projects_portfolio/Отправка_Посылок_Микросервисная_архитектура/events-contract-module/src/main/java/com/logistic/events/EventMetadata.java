package com.logistic.events;

import java.time.Instant;
import java.util.UUID;

public record EventMetadata(
        String eventId,
        Instant timestamp,
        String serviceName,
        String eventType
) {
    public static EventMetadata create(String serviceName, String eventType) {
        return new EventMetadata(
                UUID.randomUUID().toString(),
                Instant.now(),
                serviceName,
                eventType
        );
    }
}

