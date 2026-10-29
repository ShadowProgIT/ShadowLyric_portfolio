package com.example.demo.models.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "orderDishes")
public class OrderDish extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @Column(name = "total_positions")
    private Integer totalPositions;

    @Column(name = "price_at_moment")
    private BigDecimal priceAtMoment;

    public OrderDish(Order order, Dish dish, Integer totalPositions, BigDecimal priceAtMoment) {
        this.order = order;
        this.dish = dish;
        this.totalPositions = totalPositions;
        this.priceAtMoment = priceAtMoment;
    }

    public OrderDish() {

    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Integer getTotalPositions() {
        return totalPositions;
    }

    public void setTotalPositions(Integer totalPositions) {
        this.totalPositions = totalPositions;
    }

    public BigDecimal getPriceAtMoment() {
        return priceAtMoment;
    }

    public void setPriceAtMoment(BigDecimal priceAtMoment) {
        this.priceAtMoment = priceAtMoment;
    }
}