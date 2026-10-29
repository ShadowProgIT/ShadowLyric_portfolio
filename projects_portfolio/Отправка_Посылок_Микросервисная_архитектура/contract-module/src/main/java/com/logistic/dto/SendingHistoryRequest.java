package com.logistic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Schema(description = "Запрос на просмотр или полное обновление истории отправки")
public record SendingHistoryRequest(
        @Schema(
                description = "Статус отправки в момент события",
                example = "in_way",
                allowableValues = {
                        "created", "waiting", "picked_up", "in_way", "delivered", "cancelled", "lost"
                }
        )
        @NotBlank(message = "Укажите статус")
        String status,

        @Schema(description = "Местоположение", example = "Москва, сортировочный центр")
        @NotBlank(message = "Укажите местоположение")
        String location,

        @Schema(description = "Комментарий к событию", example = "Отправка принята курьером")
        String comment
) {
}

