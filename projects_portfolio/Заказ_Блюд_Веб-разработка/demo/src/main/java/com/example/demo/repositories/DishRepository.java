package com.example.demo.repositories;

import com.example.demo.models.entities.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    Optional<Dish> findByName(String name);

    boolean existsByName(String name);

    // найти блюда по имени, отсортировать по рейтингу по убыванию, результат пагинированный
    @Query("SELECT d FROM Dish d WHERE UPPER(TRIM(d.category.name)) = UPPER(TRIM(:categoryName)) ORDER BY COALESCE(d.rating, 0) DESC")
    Page<Dish> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);

    @Query("SELECT d FROM Dish d ORDER BY d.price DESC")
    Page<Dish> findByPriceDesc(Pageable pageable);

}
