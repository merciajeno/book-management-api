package com.example.bookapi.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PublicationYearValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPublicationYear {

    String message() default "Publication year must be between 1000 and the current year";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
