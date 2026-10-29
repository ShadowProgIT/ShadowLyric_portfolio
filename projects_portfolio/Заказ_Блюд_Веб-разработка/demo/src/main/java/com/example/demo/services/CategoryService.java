package com.example.demo.services;

import com.example.demo.dto.ShowCategoryDto;

import java.util.List;

public interface CategoryService {
    List<ShowCategoryDto> getAllCategories();
}
