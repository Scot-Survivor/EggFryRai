package com.comp5590.database.validators.processors;

import com.comp5590.database.entities.Booking;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.database.validators.annontations.BookingUnique;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

public class CheckBooking implements ConstraintValidator<BookingUnique, Booking> {

    private DatabaseManager db;

    public void initialize(BookingUnique constraint) {
        db = DatabaseManager.getInstance();
    }

    /**
     * Check if the booking is unique, i.e. no other booking exists with the same doctor and room at the same time
     * @param booking the booking to check
     * @param constraintValidatorContext the context
     * @return true if the booking is unique, false otherwise
     */
    @Override
    public boolean isValid(Booking booking, ConstraintValidatorContext constraintValidatorContext) {
        boolean notSameDoctorSameTime = db
            .getAllByProperty(Booking.class, "apptTime", booking.getApptTime())
            .stream()
            .filter(b -> Objects.equals(b.getDoctor().getId(), booking.getDoctor().getId()))
            .toList()
            .isEmpty();
        boolean notSameRoomSameTime = db
            .getAllByProperty(Booking.class, "apptTime", booking.getApptTime())
            .stream()
            .filter(b -> Objects.equals(b.getRoom().getRoomNumber(), booking.getRoom().getRoomNumber()))
            .toList()
            .isEmpty();
        return notSameDoctorSameTime && notSameRoomSameTime;
    }
}
