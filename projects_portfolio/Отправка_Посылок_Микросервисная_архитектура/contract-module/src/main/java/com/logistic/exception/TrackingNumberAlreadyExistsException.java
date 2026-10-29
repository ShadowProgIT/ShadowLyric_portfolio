package com.logistic.exception;

public class TrackingNumberAlreadyExistsException extends RuntimeException {
    public TrackingNumberAlreadyExistsException(String trackingNumber) {
        super("Отправка с трекинг номером '" + trackingNumber + "' уже существует");
    }
}

