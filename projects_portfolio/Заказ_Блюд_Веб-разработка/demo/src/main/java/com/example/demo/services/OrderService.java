package com.example.demo.services;


import com.example.demo.dto.ShowOrderInfoDto;

import java.util.List;

public interface OrderService {
    void createOrder(String email);

    List<ShowOrderInfoDto> getAllOrders();
}
