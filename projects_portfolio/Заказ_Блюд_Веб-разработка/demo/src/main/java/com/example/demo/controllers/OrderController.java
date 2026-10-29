package com.example.demo.controllers;

import com.example.demo.dto.ShowOrderInfoDto;
import com.example.demo.services.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public String create(
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes
    ) {
        String email = userDetails.getUsername();
        try {
            orderService.createOrder(email);
            redirectAttributes.addFlashAttribute("successMessage", "Заказ оформлен");
        } catch(IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/cart";
    }

    @GetMapping("/")
    public String getAllOrders(Model model) {
        List<ShowOrderInfoDto> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);

        return "order-list";
    }
}
