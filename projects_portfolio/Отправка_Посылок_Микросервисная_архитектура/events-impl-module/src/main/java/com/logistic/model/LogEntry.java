package com.logistic.model;

import java.time.Instant;
import java.util.Map;

public record LogEntry(
        long sequenceNumber,
        String eventId,
        String eventType,
        String source,
        Instant eventTimestamp,
        Instant receivedAt,
        Instant processedAt,
        NotificationTarget target,
        NotificationTemplate template,
        NotificationStatus status
) {

    public record NotificationTarget(
            String recipientId,
            String email,
            String phone,
            TargetType type
    ) {}

    public enum TargetType { SENDER, RECIPIENT }


    public record NotificationTemplate(
            TemplateType type,
            String subject,
            String bodyTemplate,
            Map<String, Object> variables
    ) {}

    public enum TemplateType {
        WELCOME,
        STATUS_UPDATE,
        DELIVERED,
        CANCELLED,
        ERROR
    }

    public enum NotificationStatus { PENDING, SENT, FAILED }
}

