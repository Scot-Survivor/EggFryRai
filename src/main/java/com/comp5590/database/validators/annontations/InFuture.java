package com.comp5590.database.validators.annontations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.comp5590.database.validators.processors.CheckInFutureValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to check if the date is in the future.
 */
@Target({ FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CheckInFutureValidator.class)
@Documented
public @interface InFuture {
    String message() default "{com.comp5590.validators.annontations.InFuture.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
