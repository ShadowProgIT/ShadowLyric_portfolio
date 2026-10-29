package com.example.demo.repositories;

import com.example.demo.models.entities.Cart;
import com.example.demo.models.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @EntityGraph(attributePaths = {"user", "items.dish"})
    Optional<Cart> findByUser(User user);

    boolean existsByUser(User user);
}
