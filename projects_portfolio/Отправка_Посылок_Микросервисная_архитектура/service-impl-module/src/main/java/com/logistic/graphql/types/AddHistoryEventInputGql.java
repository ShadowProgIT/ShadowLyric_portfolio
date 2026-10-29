package com.logistic.graphql.types;

public record AddHistoryEventInputGql(
        String status,
        String location,
        String comment
) {}
