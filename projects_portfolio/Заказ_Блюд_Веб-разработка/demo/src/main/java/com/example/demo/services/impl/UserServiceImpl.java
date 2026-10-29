package com.example.demo.services.impl;

import com.example.demo.dto.*;
import com.example.demo.models.entities.Role;
import com.example.demo.models.entities.User;
import com.example.demo.models.exceptions.UserNotFoundException;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Cacheable(value = "users", key = "#id")
    public ShowDetailedInfoUserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return modelMapper.map(user, ShowDetailedInfoUserDto.class);
    }

    @Cacheable(value = "users", key = "'username:' + #username")
    public ShowDetailedInfoUserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        return modelMapper.map(user, ShowDetailedInfoUserDto.class);
    }

    @Cacheable(value = "users", key = "'email:' + #email")
    public ShowDetailedInfoUserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User not found"));
        return modelMapper.map(user, ShowDetailedInfoUserDto.class);
    }

    public Page<ShowInfoUserDto> getAllUsersPageable(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<ShowInfoUserDto> userDtos = users.getContent().stream()
                .map(user -> modelMapper.map(user, ShowInfoUserDto.class))
                .collect(Collectors.toList());
        return new PageImpl<>(userDtos, pageable, users.getTotalElements());
    }

    public Page<ShowInfoUserDto> searchUsers(String query, Pageable pageable) {
        Page<User> users = userRepository
                .findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query, pageable);
        List<ShowInfoUserDto> userDtos = users.getContent().stream().map(
                user -> modelMapper.map(user, ShowInfoUserDto.class)).collect(Collectors.toList());
        return new PageImpl<>(userDtos, pageable, users.getTotalElements());
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден: " + username));
        userRepository.delete(user);
    }

    @Transactional
    public ShowDetailedInfoUserDto registerUser(UserRegistrationDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        Role userRole = roleRepository.findByRole("USER")
                .orElseGet(() -> {
                    Role newRole = new Role("USER");
                    return roleRepository.save(newRole);
                });

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setWallet(BigDecimal.ZERO);
        user.addRole(userRole);

        userRepository.save(user);

        return modelMapper.map(user, ShowDetailedInfoUserDto.class);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void makeModerator(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найден: " + username));
        Role role = roleRepository.findByRole("MODERATOR")
                .orElseGet(() -> roleRepository.save(new Role("MODERATOR")));
        user.addRole(role);
        userRepository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void makeAdmin(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + username));
        Role role = roleRepository.findByRole("ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ADMIN")));
        user.addRole(role);
        userRepository.save(user);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void addBalance(String email, BigDecimal amount) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + email));
        user.addBalance(amount);
        userRepository.save(user);
    }
}

