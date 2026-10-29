package com.example.demo.services;

import com.example.demo.dto.CartDto;

public interface CartService {
    CartDto getCartByUser(String email);

    CartDto addDishToCart(String email, String dishName);

    void removeFromCart(String email, String dishName);

    void clearCart(String email);
}
