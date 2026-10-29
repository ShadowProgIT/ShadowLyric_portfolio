package com.example.demo.controllers;

import com.example.demo.dto.CartDto;
import com.example.demo.services.CartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        CartDto cart = cartService.getCartByUser(email);
        model.addAttribute("cart", cart);
        return "cart-info";
    }

    @PostMapping("/add")
    public String addDish(@AuthenticationPrincipal UserDetails userDetails,
                          @RequestParam String dishName) {
        String email = userDetails.getUsername();
        cartService.addDishToCart(email, dishName);
        return "redirect:/dishes";
    }

    @PostMapping("/remove")
    public String removeItem(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam String dishName) {
        String email = userDetails.getUsername();
        cartService.removeFromCart(email, dishName);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        cartService.clearCart(email);
        return "redirect:/cart";
    }
}