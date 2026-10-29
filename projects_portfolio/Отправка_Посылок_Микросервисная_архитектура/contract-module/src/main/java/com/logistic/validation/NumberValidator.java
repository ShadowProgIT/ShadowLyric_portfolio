package com.logistic.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class NumberValidator implements ConstraintValidator<ValidNumber, String> {
    private static final Pattern codePattern = Pattern.compile(
            "^[A-Z]{2}\\d{2}-\\d{2}-\\d{2}$",
            Pattern.CASE_INSENSITIVE
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        String cleaned = value.replaceAll("\\s+", "");

        return codePattern.matcher(cleaned).matches();
    }
}

