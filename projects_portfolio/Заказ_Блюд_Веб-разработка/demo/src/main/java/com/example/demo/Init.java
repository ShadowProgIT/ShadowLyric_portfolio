package com.example.demo;


import com.example.demo.dto.AddDishDto;
import com.example.demo.dto.ShowDetailedInfoUserDto;
import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.models.entities.Category;
import com.example.demo.models.entities.Dish;
import com.example.demo.models.entities.Role;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.DishRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.DishService;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class Init implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(Init.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final DishRepository dishRepository;
    private final UserService userService;
    private final DishService dishService;

    public Init(RoleRepository roleRepository, UserRepository userRepository,
                CategoryRepository categoryRepository, DishRepository dishRepository,
                UserService userService, DishService dishService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.dishRepository = dishRepository;
        this.userService = userService;
        this.dishService = dishService;
    }

    @Override
    public void run(String... args) {
        Role roleUser = new Role("USER");
        Role roleModerator = new Role("MODERATOR");
        Role roleAdmin = new Role("ADMIN");

        UserRegistrationDto userOne = new UserRegistrationDto();
        userOne.setUsername("serega");
        userOne.setEmail("serega@example.com");
        userOne.setPassword("password");
        userOne.setPhone("7875834534");

        UserRegistrationDto userTwo = new UserRegistrationDto();
        userTwo.setUsername("ivan");
        userTwo.setEmail("ivan@example.com");
        userTwo.setPassword("password");
        userTwo.setPhone("787542422");

        UserRegistrationDto userThree = new UserRegistrationDto();
        userThree.setUsername("nick");
        userThree.setEmail("nick@example.com");
        userThree.setPassword("password");
        userThree.setPhone("7864334334");

        userService.registerUser(userOne);
        userService.registerUser(userTwo);
        userService.registerUser(userThree);

        userService.makeModerator("serega");
        userService.makeModerator("ivan");
        userService.makeAdmin("ivan");


        Category categoryOne = new Category();
        categoryOne.setName("Салаты");
        categoryOne.setDescription("Лёгкие и вкусные");
        categoryRepository.save(categoryOne);

        Category categoryTwo = new Category();
        categoryTwo.setName("Супы");
        categoryTwo.setDescription("Горячие");
        categoryRepository.save(categoryTwo);

        Dish olivier = new Dish(
                "Оливье",
                "Это база",
                BigDecimal.valueOf(350),
                4.9,
                24,
                280,
                categoryOne,
                new ArrayList<>()
        );
        dishRepository.save(olivier);

        AddDishDto dishOne = new AddDishDto();
        dishOne.setName("Цезарь");
        dishOne.setDescription("Гай Юлий");
        dishOne.setPrice(BigDecimal.valueOf(170));
        dishOne.setCalories(200);
        dishOne.setCategoryName("Салаты");

        AddDishDto dishTwo = new AddDishDto();
        dishTwo.setName("Борщ");
        dishTwo.setDescription("Классика к столу");
        dishTwo.setPrice(BigDecimal.valueOf(120));
        dishTwo.setCalories(170);
        dishTwo.setCategoryName("Супы");

        dishService.saveDish(dishOne);
        dishService.saveDish(dishTwo);
    }
}
