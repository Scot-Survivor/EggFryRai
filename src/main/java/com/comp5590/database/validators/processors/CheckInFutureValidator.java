package com.comp5590.database.validators.processors;

import com.comp5590.database.validators.annontations.InFuture;
import jakarta.validation.ConstraintValidator;
import java.util.Date;

/**
 * Validator to check if the date is in the future.
 */
public class CheckInFutureValidator implements ConstraintValidator<InFuture, Date> {

    @Override
    public boolean isValid(Date date, jakarta.validation.ConstraintValidatorContext constraintValidatorContext) {
        // grab current time
        long currentTime = System.currentTimeMillis();

        // check if the appointment time is in the future
        return currentTime < date.getTime();
    }
}
