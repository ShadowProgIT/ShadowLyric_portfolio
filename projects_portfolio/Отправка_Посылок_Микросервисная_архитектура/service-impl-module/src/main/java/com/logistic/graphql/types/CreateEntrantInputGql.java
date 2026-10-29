package com.logistic.graphql.types;

public record CreateEntrantInputGql(
        String firstname,
        String lastname,
        String email,
        String phone,
        String address
) {}
