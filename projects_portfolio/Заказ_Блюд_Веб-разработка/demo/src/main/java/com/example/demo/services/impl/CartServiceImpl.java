package com.example.demo.services.impl;


import com.example.demo.dto.CartDto;
import com.example.demo.models.entities.Cart;
import com.example.demo.models.entities.CartItem;
import com.example.demo.models.entities.Dish;
import com.example.demo.models.entities.User;
import com.example.demo.models.exceptions.UserNotFoundException;
import com.example.demo.repositories.CartItemRepository;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.DishRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository,
                           DishRepository dishRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.dishRepository = dishRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartDto getCartByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + email));
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });
        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public CartDto addDishToCart(String email, String dishName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + email));
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));

        Dish dish = dishRepository.findByName(dishName)
                .orElseThrow(() -> new IllegalArgumentException("Блюдо не найдено: " + dishName));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getDish().getName().equals(dishName))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().increment();
            cartItemRepository.save(existingItem.get());
        } else {
            CartItem newItem = new CartItem(cart, dish, 1);
            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    @Transactional
    public void removeFromCart(String email, String dishName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + email));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Корзина не найдена"));

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getDish().getName().equals(dishName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Блюдо не найдено в корзине: " + dishName));

        cart.getItems().remove(itemToRemove);
        cartItemRepository.delete(itemToRemove);
    }

    @Override
    @Transactional
    public void clearCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + email));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
    }
}
