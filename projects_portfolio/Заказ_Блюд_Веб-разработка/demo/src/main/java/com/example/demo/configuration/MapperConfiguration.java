package com.example.demo.configuration;

import com.example.demo.dto.*;
import com.example.demo.models.entities.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.stream.Collectors;

@Configuration
public class MapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        mapper.createTypeMap(Role.class, RoleDto.class);

        mapper.createTypeMap(User.class, ShowInfoUserDto.class);

        mapper.createTypeMap(User.class, ShowDetailedInfoUserDto.class);

        mapper.createTypeMap(Dish.class, ShowInfoDishDto.class)
                .addMapping(src -> src.getCategory().getName(), ShowInfoDishDto::setCategory);
        mapper.createTypeMap(Dish.class, ShowDetailedInfoDishDto.class)
                .addMapping(src -> src.getCategory().getName(), ShowDetailedInfoDishDto::setCategory);

        mapper.createTypeMap(CartItem.class, CartItemDto.class)
                .addMapping(src -> src.getDish().getName(), CartItemDto::setDishName)
                .addMapping(src -> src.getDish().getPrice(), CartItemDto::setPrice)
                .addMapping(CartItem::getQuantity, CartItemDto::setQuantity);

        mapper.createTypeMap(Cart.class, CartDto.class)
                .addMapping(src -> src.getUser().getEmail(), CartDto::setEmail)
                .addMapping(Cart::getItems, CartDto::setItems);

        mapper.typeMap(Order.class, ShowOrderInfoDto.class)
                .addMapping(src -> src.getUser().getEmail(), ShowOrderInfoDto::setEmail)
                .addMapping(
                        Order::getAmount,
                        (dest, value) -> dest.setAmount(value == null ? 0.0 : ((BigDecimal) value).doubleValue())
                );

        return mapper;
    }
}

