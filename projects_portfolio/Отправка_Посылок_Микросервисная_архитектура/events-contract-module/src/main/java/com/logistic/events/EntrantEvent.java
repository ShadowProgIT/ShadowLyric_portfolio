package com.logistic.events;

public sealed interface EntrantEvent {

    record Created(
            Long entrantId,
            String firstname,
            String lastname,
            String email,
            String phone,
            String address
    ) implements EntrantEvent {}

    record Deleted(
            Long entrantId,
            String firstname,
            String lastname,
            String phone
    ) implements EntrantEvent {}
}

