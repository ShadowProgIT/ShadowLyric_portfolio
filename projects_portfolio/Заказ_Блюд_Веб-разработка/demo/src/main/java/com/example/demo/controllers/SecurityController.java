package com.example.demo.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// для обработки выбросов Spring Security (вызванных в цепочки фильтрации)
@Controller
public class SecurityController {

    @GetMapping("/errors/403")
    public String error403(Model model) {
        model.addAttribute("errorTitle", "Доступ запрещён");
        model.addAttribute("errorMessage", "У вас нет прав для просмотра этой страницы.");
        model.addAttribute("errorCode", "403");
        return "errors/no-have-permissions";
    }
}
