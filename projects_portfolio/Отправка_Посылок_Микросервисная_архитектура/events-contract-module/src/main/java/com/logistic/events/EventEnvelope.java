package com.logistic.events;

public record EventEnvelope<T>(
        EventMetadata metadata,
        T payload
) {

    public static <T> EventEnvelope<T> wrap(T payload, String serviceName, String eventType) {
        return new EventEnvelope<>(
                EventMetadata.create(serviceName, eventType),
                payload
        );
    }
}

