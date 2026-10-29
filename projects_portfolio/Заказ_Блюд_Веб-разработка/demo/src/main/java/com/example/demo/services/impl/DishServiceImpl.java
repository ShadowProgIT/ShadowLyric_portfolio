package com.example.demo.services.impl;

import com.example.demo.dto.*;
import com.example.demo.models.entities.Category;
import com.example.demo.models.entities.Dish;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.DishRepository;
import com.example.demo.services.DishService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.Random;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService {
    private final Random random = new Random();
    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private static final Logger log = LoggerFactory.getLogger(DishServiceImpl.class);

    public DishServiceImpl(DishRepository dishRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.dishRepository = dishRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Cacheable(value = "dishes", key = "#name")
    public ShowDetailedInfoDishDto getDishByName(String name) {
        Dish dish = dishRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Блюдо не найдено!"));
        return modelMapper.map(dish, ShowDetailedInfoDishDto.class);
    }

    public Page<ShowInfoDishDto> getTopFiveRatingDishes(String categoryName, int page, int size) {
        log.info("getTopFiveRatingDishes: categoryName = [{}], page = {}, size = {}", categoryName, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "rating"));
        Page<Dish> dishPage = dishRepository.findByCategoryName(categoryName, pageable);

        return dishPage.map(dish -> modelMapper.map(dish, ShowInfoDishDto.class));
    }

    public Page<ShowInfoDishDto> getAllDishesPageable(Pageable pageable) {
        Page<Dish> dishes = dishRepository.findAll(pageable);
        List<ShowInfoDishDto> dishDtos = dishes.getContent().stream()
                .map(dish -> modelMapper.map(dish, ShowInfoDishDto.class))
                .collect(Collectors.toList());
        return new PageImpl<>(dishDtos, pageable, dishes.getTotalElements());
    }

    @Transactional
    @CacheEvict(value = "dishes", key = "#name")
    public void deleteDish(String name) {
        Dish dish = dishRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Блюдо не найдено"));
        dishRepository.delete(dish);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public void saveDish(AddDishDto addDishDto) {
        Category category = categoryRepository.findByName(addDishDto.getCategoryName())
                .orElseThrow(() -> new EntityNotFoundException("Категория не найдена"));

        if (dishRepository.existsByName(addDishDto.getName())) {
            throw new IllegalArgumentException("Такое блюдо уже существует");
        }

        Dish dish = new Dish();
        dish.setName(addDishDto.getName());
        dish.setDescription(addDishDto.getDescription());
        dish.setPrice(addDishDto.getPrice());
        dish.setCalories(addDishDto.getCalories());
        dish.setCategory(category);
        dish.setRating(random.nextInt(51) / 10.0);
        dish.setReviewsCount(random.nextInt(327));
        dishRepository.save(dish);
    }

    @Override
    public void editPrice(String dishName, BigDecimal price) {
        Dish dish = dishRepository.findByName(dishName)
                 .orElseThrow(() -> new EntityNotFoundException("Блюдо не найдено!"));
        dish.setPrice(price);
        dishRepository.save(dish);
    }
}
