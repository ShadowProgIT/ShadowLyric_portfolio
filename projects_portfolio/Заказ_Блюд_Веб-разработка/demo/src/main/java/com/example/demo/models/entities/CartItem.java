package com.example.demo.models.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    public CartItem() {

    }

    public CartItem(Cart cart, Dish dish, Integer quantity) {
        this.cart = cart;
        this.dish = dish;
        this.quantity = quantity;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void increment() {
        this.quantity++;
    }

    public void decrement() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }
}
