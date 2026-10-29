package com.example.demo.services.impl;

import com.example.demo.dto.ShowOrderInfoDto;
import com.example.demo.models.entities.*;
import com.example.demo.models.exceptions.UserNotFoundException;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.OrderDishRepository;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderDishRepository orderDishRepository;
    private final Random random = new Random();
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository,
                            CartRepository cartRepository, OrderDishRepository orderDishRepository,
                            ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderDishRepository = orderDishRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void createOrder(String email) {
        log.info("Заказ создан");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + email));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Корзина не найдена"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Корзина пуста");
        }


        BigDecimal total = cart.getItems().stream()
                .map(item -> BigDecimal.valueOf(item.getDish().getPrice())
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if(user.getWallet().compareTo(total) < 0) {
            throw new IllegalArgumentException("Средств для оплаты недостаточно");
        }

        Order order = new Order();
        order.setUser(user);
        order.setAmount(total);
        order.setDate(LocalDateTime.now());
        order.setStatus("В доставке");
        order.setDeliveryTime(random.nextInt(10, 60));
        orderRepository.save(order);

        List<OrderDish> orderDishes = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            OrderDish orderDish = new OrderDish();
            orderDish.setOrder(order);
            orderDish.setDish(item.getDish());
            orderDish.setTotalPositions(item.getQuantity());
            orderDish.setPriceAtMoment(BigDecimal.valueOf(item.getDish().getPrice()));
            orderDishes.add(orderDish);
        }

        orderDishRepository.saveAll(orderDishes);
        user.setWallet(user.getWallet().subtract(total));
        userRepository.save(user);

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public List<ShowOrderInfoDto> getAllOrders() {
        return orderRepository.findAllWithUser().stream()
                .map(order -> modelMapper.map(order, ShowOrderInfoDto.class))
                .collect(Collectors.toList());
    }
}
