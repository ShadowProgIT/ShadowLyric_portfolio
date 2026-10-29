package com.example.demo.controllers;

import com.example.demo.dto.*;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", ""})
    public String listUsersPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(required = false) String search,
            Model model
    ) {
        if (!"username".equals(sortBy) && !"email".equals(sortBy)) {
            sortBy = "username";
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<ShowInfoUserDto> userPage = userService.getAllUsersPageable(pageable);

        // поиск
        if (search != null && !search.trim().isEmpty()) {
            userPage = userService.searchUsers(search.trim(), pageable);
            model.addAttribute("search", search.trim());
        } else { // вывод всех юзеров
            userPage = userService.getAllUsersPageable(pageable);
        }

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);

        return "user-pageable";
    }

    @GetMapping("/{username}")
    public String getUser(@PathVariable String username, Model model) {
        ShowDetailedInfoUserDto user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "user-info";
    }

    @GetMapping("/delete/{username}")
    public String deleteUser(@PathVariable String username, RedirectAttributes redirectAttributes) {
        log.debug("Удаление пользователя: {}", username);
        userService.deleteUser(username);
        redirectAttributes.addFlashAttribute("successMessage",
                "Пользователь '" + username + "' успешно удалён");
        return "redirect:/users";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("registrationDto", new UserRegistrationDto());
        return "user-add";
    }

    @PostMapping("/add")
    public String addUser(
            @Valid UserRegistrationDto userDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        log.debug("Добавление пользователя: {}", userDto.getUsername());
        if (bindingResult.hasErrors()) {
            log.warn("Ошибки при добавлении пользователя: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("registrationDto", userDto);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.registrationDto",
                    bindingResult
            );
            return "redirect:/users/add";
        }

        userService.registerUser(userDto);
        log.info("Пользователь зарегистрирован: {}", userDto.getUsername());

        return "redirect:/users/";
    }

    @GetMapping("/profile")
    public String viewUserProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        ShowDetailedInfoUserDto userProfile = userService.getUserByEmail(email);

        model.addAttribute("profile", userProfile);

        return "user-profile";
    }

    @PostMapping("/profile/recharge")
    public String addBalance(
            @RequestParam("amount") BigDecimal amount,
            RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        try {
            userService.addBalance(email, amount);
            redirectAttributes.addFlashAttribute("successMessage", amount + "руб.");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users/profile";
    }
}


