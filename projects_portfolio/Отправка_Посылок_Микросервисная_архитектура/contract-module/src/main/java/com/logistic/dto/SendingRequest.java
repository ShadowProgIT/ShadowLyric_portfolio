package com.logistic.dto;

import com.logistic.validation.ValidNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Запрос на создание или полное обновление отправки")
public record SendingRequest(
        @Schema(description = "Трекинг номер отправления", example = "RU12-23-42")
        @NotBlank(message = "Трекинг номер не должен быть пустым")
        @ValidNumber
        String trackingNumber,

        @Schema(description = "Вес отправки в кг", example = "10.0")
        @NotNull(message = "Укажите вес отправки")
        @Positive(message = "Вес должен быть положительным числом")
        Double weight,

        @Schema(description = "Габариты (ДхШхВ)", example = "10x30x50")
        @NotBlank(message = "Укажите габариты отправки")        
        String size,

        @Schema(description = "ID получателя")
        @NotNull(message = "Укажите ID получателя")
        Long recipientId,

        @Schema(description = "ID отправителя")
        @NotNull(message = "Укажите ID отправителя")
        Long senderId

) {
}

