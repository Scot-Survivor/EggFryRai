package com.comp5590.database.validators.processors;

import com.comp5590.database.entities.Insurance;
import com.comp5590.database.validators.annontations.ChronologicalDates;
import jakarta.validation.ConstraintValidator;

public class CheckChronologicalDatesValidator implements ConstraintValidator<ChronologicalDates, Insurance> {

    @Override
    public boolean isValid(
        Insurance insurance,
        jakarta.validation.ConstraintValidatorContext constraintValidatorContext
    ) {
        // ensures that the start date is before the end date
        return insurance.getStartDate().before(insurance.getEndDate());
    }
}
