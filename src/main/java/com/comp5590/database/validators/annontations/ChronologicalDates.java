package com.comp5590.database.validators.annontations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.comp5590.database.validators.processors.CheckChronologicalDatesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to check if the dates are in chronological order.
 */
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CheckChronologicalDatesValidator.class)
@Documented
public @interface ChronologicalDates {
    String message() default "{com.comp5590.validators.annontations.ChronologicalDates.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
