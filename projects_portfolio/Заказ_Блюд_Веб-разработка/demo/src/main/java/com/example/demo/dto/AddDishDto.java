package com.example.demo.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class AddDishDto {

    @NotBlank(message = "Название обязательно") @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
    private String name;

    private String description;

    @NotNull(message = "Цена обязательна") @Positive(message = "Цена должна быть положительной")
    private BigDecimal price;

    @PositiveOrZero
    private Integer calories;

    @NotBlank(message = "Выберите категорию")
    private String categoryName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
