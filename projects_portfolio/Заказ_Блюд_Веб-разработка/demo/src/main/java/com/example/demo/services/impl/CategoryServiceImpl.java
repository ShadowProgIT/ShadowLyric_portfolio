package com.example.demo.services.impl;

import com.example.demo.dto.ShowCategoryDto;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Cacheable(value = "categories")
    public List<ShowCategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> modelMapper.map(category, ShowCategoryDto.class))
                .collect(Collectors.toList());
    }
}
