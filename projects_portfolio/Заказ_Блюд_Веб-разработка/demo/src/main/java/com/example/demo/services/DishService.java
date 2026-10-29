package com.example.demo.services;

import com.example.demo.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DishService {

    ShowDetailedInfoDishDto getDishByName(String name);

    Page<ShowInfoDishDto> getAllDishesPageable(Pageable pageable);

    void deleteDish(String name);

    void saveDish(AddDishDto addDishDto);

    Page<ShowInfoDishDto> getTopFiveRatingDishes(String categoryName, int page, int size);

    void editPrice(String dishName, BigDecimal price);
}
