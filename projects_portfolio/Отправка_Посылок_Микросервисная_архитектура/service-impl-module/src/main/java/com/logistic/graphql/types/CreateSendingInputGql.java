package com.logistic.graphql.types;

public record CreateSendingInputGql(
        String trackingNumber,
        Double weight,
        String size,
        String recipientId,
        String senderId
) {}
