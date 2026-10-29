package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public class EditDishPriceDto {
    @DecimalMin(value = "0.1", message = "Цена должна быть положительной")
    private BigDecimal price;
    private String dishName;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }
}
