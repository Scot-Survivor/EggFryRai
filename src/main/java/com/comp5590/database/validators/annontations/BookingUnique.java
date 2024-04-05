package com.comp5590.database.validators.annontations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.comp5590.database.validators.processors.CheckBooking;
import jakarta.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to ensure booking is properly unique
 */
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CheckBooking.class)
@Documented
public @interface BookingUnique {
    String message() default "{com.comp5590.validators.annontations.BookingUnique.message}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
