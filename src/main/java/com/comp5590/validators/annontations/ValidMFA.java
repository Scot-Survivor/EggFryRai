package com.comp5590.validators.annontations;

import com.comp5590.validators.processors.CheckMFAValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CheckMFAValidator.class)
@Documented
public @interface ValidMFA {
    String message() default "{com.comp5590.validators.annontations.Valid2FA.message}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
