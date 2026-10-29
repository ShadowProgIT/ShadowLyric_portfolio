package com.example.demo.controllers;


import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/register")
    public String showRegForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "user-registration";
    }

    @PostMapping("/users/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegistrationDto dto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "user-registration";
        }

        if (userService.existsByEmail(dto.getEmail())) {
            model.addAttribute("emailError", "Email already exists");
            return "user-registration";
        }

        userService.registerUser(dto);
        return "redirect:/user-login";
    }

    @GetMapping("/users/login")
    public String login() {
        return "user-login";
    }
}
