package com.logistic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@Schema(description = "Запрос на регистрацию участника отправки")
public record EntrantRequest(
        @Schema(description = "Имя участника", example = "Ivan")
        @NotBlank(message = "Укажите имя")
        String firstname,

        @Schema(description = "Фамилия участника", example = "Ivanov")
        @NotBlank(message = "Укажите фамилию")
        String lastname,

        @Schema(description = "E-mail адрес участника", example = "example@mail.ru")
        @Email
        @NotBlank(message = "Укажите Ваш e-mail")
        String email,

        @Schema(description = "Телефонный номер участника", example = "88005553535")
        @NotBlank(message = "Укажите Ваш телефонный номер")
        String phone,

        @Schema(description = "Адрес участника", example = "Ул.Пушкина, д.Колотушкина")
        @NotBlank(message = "Укажите Ваш адрес")
        String address
) {
}

