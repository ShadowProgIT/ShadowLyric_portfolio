package com.example.demo.dto;

public class ShowOrderInfoDto {
    private String email;
    private String status;
    private double amount;
    private int deliveryTime;

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

}
