package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class UserRegistrationDto {

    @NotEmpty(message = "Имя пользователя не должно быть пустым") @Size(min = 3, max = 30)
    private String username;

    @NotBlank(message = "Почта не должна быть пустой") @Email(message = "E-mail некорректен")
    private String email;

    @NotEmpty(message = "Пароль не должен быть пустым") @Size(min = 6)
    private String password;

    @NotEmpty(message = "Телефон не должен быть пустым")
    private String phone;

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }
}