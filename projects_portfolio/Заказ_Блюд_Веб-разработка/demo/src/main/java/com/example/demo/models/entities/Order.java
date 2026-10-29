package com.example.demo.models.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    private String status;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "delivery_time")
    private int deliveryTime;


    @Column(name = "date")
    private LocalDateTime date;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDish> orderDishes = new ArrayList<>();

    public Order(User user, String status, BigDecimal amount, int deliveryTime, LocalDateTime date, List<OrderDish> orderDishes) {
        this.user = user;
        this.status = status;
        this.amount = amount;
        this.deliveryTime = deliveryTime;
        this.date = date;
        this.orderDishes = orderDishes;
    }

    public Order() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() { return amount; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public LocalDateTime getDate() { return date; }

    public void setDate(LocalDateTime date) { this.date = date; }

    public List<OrderDish> getOrderDishes() {
        return orderDishes;
    }

    public void setOrderDishes(List<OrderDish> orderDishes) {
        this.orderDishes = orderDishes;
    }
}