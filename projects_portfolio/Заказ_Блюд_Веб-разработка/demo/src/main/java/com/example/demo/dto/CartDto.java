package com.example.demo.dto;

import java.util.List;

public class CartDto {
    private String email;
    private List<CartItemDto> items;
    private Double totalPrice;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }

    public Double getTotalPrice() {
        if (items == null) {
            return 0.0;
        } else {
            return items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        }
    }
}
