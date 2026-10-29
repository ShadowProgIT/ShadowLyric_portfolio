package com.example.demo.controllers;

import com.example.demo.models.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFound(UserNotFoundException ex, Model model) {
        log.warn("Пользователь не найден: {}", ex.getMessage());
        model.addAttribute("errorTitle", "Пользователь не найден");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", "404");
        return "errors/user-not-found";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String accessDenied(AccessDeniedException ex, Model model) {
        log.warn("Доступ запрещён: {}", ex.getMessage());
        model.addAttribute("errorTitle", "Доступ запрещён");
        model.addAttribute("errorMessage", "У вас нет прав для просмотра этой страницы.");
        model.addAttribute("errorCode", "403");
        return "errors/no-have-permissions";
    }
}
