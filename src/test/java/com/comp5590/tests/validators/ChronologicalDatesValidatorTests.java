package com.comp5590.tests.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.comp5590.entities.Insurance;
import com.comp5590.tests.basic.SetupTests;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.Test;

public class ChronologicalDatesValidatorTests extends SetupTests {

    @Test
    public void testStartDateBeforeEndDate() {
        Validator validator = Validation
            .byProvider(HibernateValidator.class)
            .configure()
            .buildValidatorFactory()
            .getValidator();

        Insurance insurance = new Insurance();

        // make 2 dates, one for the start date and one for the end date
        Date dateStart = Date.from(LocalDate.of(2022, 12, 25).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateEnd = Date.from(LocalDate.of(2023, 12, 25).atStartOfDay(ZoneId.systemDefault()).toInstant());

        insurance.setStartDate(dateStart);
        insurance.setEndDate(dateEnd);

        // check if the start date is before the end date
        assertTrue(validator.validate(insurance).isEmpty());
    }

    @Test
    public void testStartDateAfterEndDate() {
        Validator validator = Validation
            .byProvider(HibernateValidator.class)
            .configure()
            .buildValidatorFactory()
            .getValidator();

        Insurance insurance = new Insurance();

        // make 2 dates, one for the start date and one for the end date
        Date dateStart = Date.from(LocalDate.of(2023, 12, 25).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateEnd = Date.from(LocalDate.of(2022, 12, 25).atStartOfDay(ZoneId.systemDefault()).toInstant());

        insurance.setStartDate(dateStart);
        insurance.setEndDate(dateEnd);

        // check if the start date is before the end date
        assertFalse(validator.validate(insurance).isEmpty());
    }

    @Test
    public void testStartDateEqualsEndDate() {
        Validator validator = Validation
            .byProvider(HibernateValidator.class)
            .configure()
            .buildValidatorFactory()
            .getValidator();

        Insurance insurance = new Insurance();

        // make 2 dates, one for the start date and one for the end date
        Date dateStart = Date.from(LocalDate.of(2022, 12, 25).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateEnd = Date.from(LocalDate.of(2022, 12, 25).atStartOfDay(ZoneId.systemDefault()).toInstant());

        insurance.setStartDate(dateStart);
        insurance.setEndDate(dateEnd);

        // check if the start date is before the end date (should return false cos they
        // are equal)
        assertFalse(validator.validate(insurance).isEmpty());
    }
}
