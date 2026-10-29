package com.logistic.events;

public sealed interface SendingEvent {
    record Created(
            Long id,
            String trackingNumber,
            String status,
            String weight,
            String size,
            String createdAt,
            Long recipientId,
            Long senderId
    ) implements SendingEvent {}

    record Update(
            Long id,
            String trackingNumber,
            String oldStatus,
            String newStatus
    ) implements SendingEvent {}

    record Deleted(
            Long id,
            String trackingNumber
    ) implements SendingEvent {}

    record Enriched(
            Long id,
            String trackingNumber,
            int estimatedMinutes,
            String routeStatus
    ) implements SendingEvent {}
}

