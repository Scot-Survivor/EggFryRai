package com.comp5590.validators.annontations;

import com.comp5590.enums.UserRole;
import com.comp5590.validators.processors.CheckRoleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CheckRoleValidator.class)
@Documented
public @interface RequiredRole {
    String message() default "{com.comp5590.validators.annontations.RequiredRole.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    UserRole value();
}
