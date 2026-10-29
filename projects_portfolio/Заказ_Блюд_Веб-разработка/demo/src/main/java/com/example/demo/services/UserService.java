package com.example.demo.services;

import com.example.demo.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    ShowDetailedInfoUserDto getUserById(Long id);

    ShowDetailedInfoUserDto getUserByUsername(String username);

    ShowDetailedInfoUserDto getUserByEmail(String email);

    Page<ShowInfoUserDto> getAllUsersPageable(Pageable pageable);

    Page<ShowInfoUserDto> searchUsers(String query, Pageable pageable);

    void deleteUser(String username);

    ShowDetailedInfoUserDto registerUser(UserRegistrationDto userRegistrationDto);

    boolean existsByEmail(String email);

    void makeModerator(String username);

    void makeAdmin(String username);

    void addBalance(String email, BigDecimal amount);
}
