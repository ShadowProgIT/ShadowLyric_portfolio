package com.logistic.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNumber {
    String message() default "Некорректный формат кода. Ожидаемый формат: RU12-23-42 " +
            "(RU + 2 цифры + дефис + 2 цифры + дефис + 2 цифры)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

