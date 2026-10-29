package com.example.demo.controllers;

import com.example.demo.dto.ShowDetailedInfoUserDto;
import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/test")
public class TestController {

    private final UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{username}")
    public String getTestUser(@PathVariable String username, Model model){
        ShowDetailedInfoUserDto user = userService.getUserByUsername(username);

        model.addAttribute("user", user);
        return "test-info";
    }

    @GetMapping("/user/register")
    public String editProfile(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "user-edit-form";
    }

    @PostMapping("/user/register")
    public String registerUser(
            @Valid UserRegistrationDto userRegistrationDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()) {
            return "user-edit-form";
        }

        userService.registerUser(userRegistrationDto);
        return "redirect:/test/";
    }
}
