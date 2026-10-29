package com.example.demo.models.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dishes")
public class Dish extends BaseEntity {

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "reviews_count")
    private Integer reviewsCount; // количество отзывов

    @Column(name = "calories")
    private Integer calories;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDish> orderDishes = new ArrayList<>();

    public Dish(String name, String description, BigDecimal price, Double rating,
                Integer reviewsCount, Integer calories, Category category,
                List<OrderDish> orderDishes) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.reviewsCount = reviewsCount;
        this.calories = calories;
        this.category = category;
        this.orderDishes = orderDishes;
    }

    public Dish() {

    }

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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<OrderDish> getOrderDishes() {
        return orderDishes;
    }

    public void setOrderDishes(List<OrderDish> orderDishes) {
        this.orderDishes = orderDishes;
    }
}
