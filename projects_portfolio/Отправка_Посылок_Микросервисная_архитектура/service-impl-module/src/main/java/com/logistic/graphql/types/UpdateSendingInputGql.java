package com.logistic.graphql.types;

public record UpdateSendingInputGql(
        String trackingNumber,
        Double weight,
        String size,
        String recipientId,
        String senderId
) {}
