package com.comp5590.tests.basic.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.comp5590.database.entities.Booking;
import com.comp5590.tests.basic.SetupTests;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.Test;

public class InFutureValidatorTests extends SetupTests {

    @Test
    public void testBookingSucceedsOnFutureDateTime() {
        // make new validator
        Validator validator = Validation
            .byProvider(HibernateValidator.class)
            .configure()
            .buildValidatorFactory()
            .getValidator();

        // create a new booking
        Booking booking = new Booking();

        // set the date to a future date
        booking.setApptTime(new java.util.Date(System.currentTimeMillis() + 1000000));

        // validate the booking by checking if the validator returns an empty set
        // (meaning that the booking is valid because it is in the future)
        assertTrue(validator.validate(booking).isEmpty());
    }

    @Test
    public void testBookingFailsOnPastDateTime() {
        // make new validator
        Validator validator = Validation
            .byProvider(HibernateValidator.class)
            .configure()
            .buildValidatorFactory()
            .getValidator();

        // create a new booking
        Booking booking = new Booking();

        // set the date to a past date
        booking.setApptTime(new java.util.Date(System.currentTimeMillis() - 1000000));

        // validate the booking by checking if the validator returns a non-empty set
        // (meaning that the booking is invalid because it is in the past)
        assertFalse(validator.validate(booking).isEmpty());
    }
}
