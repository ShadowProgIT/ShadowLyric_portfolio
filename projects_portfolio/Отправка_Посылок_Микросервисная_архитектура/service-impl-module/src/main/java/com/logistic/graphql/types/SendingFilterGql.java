package com.logistic.graphql.types;

public record SendingFilterGql(
        String status,
        String trackingNumber,
        String senderId,
        String recipientId
) {}
