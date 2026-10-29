package com.logistic.exception;

import com.logistic.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String BASE_PROBLEM_URI = "https://api.logistic.pro/problems/";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
                                                                HttpServletRequest req) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        BASE_PROBLEM_URI + "resource-not-found",
                        "Ресурс не найден",
                        ex.getMessage(),
                        req.getRequestURI(),
                        Instant.now(),
                        null
                ));
    }

    @ExceptionHandler(TrackingNumberAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleTrackingNumberConflict(TrackingNumberAlreadyExistsException ex,
                                                                      HttpServletRequest req) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        BASE_PROBLEM_URI + "tracking-number-conflict",
                        "Конфликт трекинг-номера",
                        ex.getMessage(),
                        req.getRequestURI(),
                        Instant.now(),
                        null
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest req) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ErrorResponse.FieldError(
                        fe.getField(),
                        fe.getRejectedValue(),
                        fe.getDefaultMessage()
                ))
                .toList();

        String detail = fieldErrors.stream()
                .map(fe -> fe.field() + ": " + fe.message())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Ошибка валидации входных данных");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        BASE_PROBLEM_URI + "validation-error",
                        "Ошибка валидации",
                        detail,
                        req.getRequestURI(),
                        Instant.now(),
                        fieldErrors
                ));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex,
                                                            HttpServletRequest req) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        BASE_PROBLEM_URI + "resource-conflict",
                        "Конфликт ресурсов",
                        ex.getMessage(),
                        req.getRequestURI(),
                        Instant.now(),
                        null
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest req) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        BASE_PROBLEM_URI + "internal-error",
                        "Внутренняя ошибка сервера",
                        "Произошла непредвиденная ошибка",
                        req.getRequestURI(),
                        Instant.now(),
                        null
                ));
    }
}
