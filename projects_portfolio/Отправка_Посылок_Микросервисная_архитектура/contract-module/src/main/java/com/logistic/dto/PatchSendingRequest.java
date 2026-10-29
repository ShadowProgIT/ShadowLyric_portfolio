package com.logistic.dto;

import com.logistic.validation.ValidNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

@Schema(description = "Запрос на частичное обновление данных отправки. Передать только те поля, которые нужно изменить")
public record PatchSendingRequest(
        @Schema(description = "Трекинг номер отправления", example = "RU12-23-42")
        @ValidNumber
        String trackingNumber,

        @Schema(description = "Вес посылки в кг", example = "10.0")
        @Positive(message = "Вес должен быть положительным числом")
        Double weight,

        @Schema(description = "Габариты (ДхШхВ)", example = "10x30x50")
        String size,

        @Schema(description = "ID получателя")
        Long recipientId,

        @Schema(description = "ID отправителя")
        Long senderId

) {
}

