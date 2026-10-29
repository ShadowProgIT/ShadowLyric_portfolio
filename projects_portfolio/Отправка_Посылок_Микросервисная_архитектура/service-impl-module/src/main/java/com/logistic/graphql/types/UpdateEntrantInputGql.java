package com.logistic.graphql.types;

public record UpdateEntrantInputGql(
        String firstname,
        String lastname,
        String email,
        String phone,
        String address
) {}
