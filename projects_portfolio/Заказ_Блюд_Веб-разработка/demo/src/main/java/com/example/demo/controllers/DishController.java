package com.example.demo.controllers;


import com.example.demo.dto.*;
import com.example.demo.services.CategoryService;
import com.example.demo.services.DishService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller @RequestMapping("/dishes")
public class DishController {
    private final DishService dishService;
    private final CategoryService categoryService;
    private static final Logger log = LoggerFactory.getLogger(DishController.class);

    public DishController(DishService dishService, CategoryService categoryService) {
        this.dishService = dishService;
        this.categoryService = categoryService;
    }

    @GetMapping({"/", ""})
    public String listDishesPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(required = false) String search,
            Model model) {

        int size = 6;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<ShowInfoDishDto> dishPage = dishService.getAllDishesPageable(pageable);

        model.addAttribute("dishPage", dishPage);
        model.addAttribute("search", search);

        return "dish-list";
    }

    @GetMapping("/top")
    public String getTopFiveDishes(
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        int size = 5;
        List<ShowCategoryDto> allCategories = categoryService.getAllCategories();
        if (category.isEmpty() && !allCategories.isEmpty()) {
            category = allCategories.get(0).getName();
        }

        Page<ShowInfoDishDto> dishPage;
        if(category.isEmpty()) {
            dishPage = Page.empty();
        } else {
            dishPage = dishService.getTopFiveRatingDishes(category, page, size);
        }

        model.addAttribute("dishPage", dishPage);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("allCategories", allCategories);

        return "dish-top";
    }

    @GetMapping("/{name}")
    public String dishDetails(@PathVariable String name, Model model) {
        ShowDetailedInfoDishDto dishDto = dishService.getDishByName(name);
        model.addAttribute("dish", dishDto);
        model.addAttribute("editPriceDto", new EditDishPriceDto());
        return "dish-info";
    }

    @GetMapping("/delete/{name}")
    public String deleteDish(@PathVariable String name, RedirectAttributes redirectAttributes) {
        log.debug("Удаление блюда: {}", name);
        dishService.deleteDish(name);
        redirectAttributes.addFlashAttribute("successMessage", "Блюдо '" +
                name + "' успешно удалено!");
        return "redirect:/dishes";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        log.debug("Отображение формы добавления блюда");
        if (!model.containsAttribute("addDishDto")) {
            model.addAttribute("addDishDto", new AddDishDto());
        }
        List<ShowCategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "dish-add";
    }

    @PostMapping("/add")
    public String addDish(
            @Valid AddDishDto addDishDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        log.debug("Добавление блюда: {}", addDishDto.getName());

        if (bindingResult.hasErrors()) {
            log.warn("Ошибки при добавлении блюда: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("addDishDto", addDishDto);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.addDishDto",
                    bindingResult
            );
            return "redirect:/dishes/add";
        }

        dishService.saveDish(addDishDto);
        log.info("Блюдо добавлено: {}", addDishDto.getName());

        redirectAttributes.addFlashAttribute("successMessage", "Блюдо '" + addDishDto.getName() + "' успешно добавлено!");
        return "redirect:/dishes";
    }

    @PostMapping("/{name}/edit-price")
    public String editPrice(
            @PathVariable String dishName,
            @Valid @ModelAttribute("editPriceDto") EditDishPriceDto dto,
            BindingResult bindingResult,
            Model model
            ) {
        if (bindingResult.hasErrors()) {
            ShowDetailedInfoDishDto dishDto = dishService.getDishByName(dishName);
            model.addAttribute("dish", dishDto);
            model.addAttribute("editPriceDto", dto);
            return "dish-info";
        }

        dishService.editPrice(dto.getDishName(), dto.getPrice());
        return "redirect:/dishes/" + dishName;
    }
}
