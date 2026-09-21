package com.example.bookapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class PublicationYearValidator
        implements ConstraintValidator<ValidPublicationYear, Integer> {

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context) {
        if (year == null) {
            return true; // @NotNull handles null.
        }

        int currentYear = Year.now().getValue();
        return year >= 1000 && year <= currentYear;
    }
}
