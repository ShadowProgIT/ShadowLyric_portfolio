package com.logistic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "Запрос на частичное обновление данных участника. Передать только те поля, которые нужно изменить")
public record PatchEntrantRequest(
        @Schema(description = "Имя участника", example = "Ivan")
        String firstname,

        @Schema(description = "Фамилия участника", example = "Ivanov")
        String lastname,

        @Schema(description = "E-mail адрес участника", example = "example@mail.ru")
        @Email
        String email,

        @Schema(description = "Телефонный номер участника", example = "88005553535")
        String phone,

        @Schema(description = "Адрес участника", example = "Ул.Пушкина, д.Колотушкина")
        String address
) {
}

