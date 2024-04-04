package com.comp5590.tests.basic.validators;

import static org.junit.jupiter.api.Assertions.*;

import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.Booking;
import com.comp5590.database.entities.Room;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.tests.basic.SetupTests;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Date;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.Test;

public class CheckBookingValidatorTests extends SetupTests {

    private Validator getValidator() {
        return Validation.byProvider(HibernateValidator.class).configure().buildValidatorFactory().getValidator();
    }

    private Room createRoom(String roomNumber) {
        Address address = createAddress();
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setAddress(address);
        return DatabaseManager.getInstance().saveGet(room);
    }

    private Date getDateOneDayInFuture() {
        return new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
    }

    @Test
    public void testBookingFailsSameRoomSameTime() {
        User doc1 = createDoctor("doc1@example.com", "password");
        User doc2 = createDoctor("doc2@example.com", "password");
        User pat = createPatient("pat1@example.com", "password");
        Room room = createRoom("001");
        Date timeNow = this.getDateOneDayInFuture();
        Booking booking = new Booking();
        booking.setDoctor(doc1);
        booking.setPatient(pat);
        booking.setApptTime(timeNow);
        booking.setRoom(room);

        assertTrue(
            getValidator().validate(booking).isEmpty(),
            "Booking should be valid but wasn't for reason: " + getValidator().validate(booking)
        );
        DatabaseManager.getInstance().save(booking);

        Booking booking2 = new Booking();
        booking2.setDoctor(doc2);
        booking2.setPatient(pat);
        booking2.setApptTime(timeNow);
        booking2.setRoom(room);

        assertFalse(getValidator().validate(booking2).isEmpty());
    }

    @Test
    public void testBookingFailsSameDoctorSameTime() {
        User doc1 = createDoctor("doc1@example.com", "password");
        User pat = createPatient("pat1@example.com", "password");
        Room room1 = createRoom("001");
        Room room2 = createRoom("002");
        Date timeNow = this.getDateOneDayInFuture();

        Booking booking = new Booking();
        booking.setDoctor(doc1);
        booking.setPatient(pat);
        booking.setApptTime(timeNow);
        booking.setRoom(room1);

        assertTrue(
            getValidator().validate(booking).isEmpty(),
            "Booking should be valid but wasn't for reason: " + getValidator().validate(booking)
        );
        DatabaseManager.getInstance().save(booking);

        Booking booking2 = new Booking();
        booking2.setDoctor(doc1);
        booking2.setPatient(pat);
        booking2.setApptTime(timeNow);
        booking2.setRoom(room2);

        assertFalse(getValidator().validate(booking2).isEmpty());
    }

    @Test
    public void testBookingSucceedsDifferentDoctorDifferentRoomDifferentTime() {
        User doc1 = createDoctor("doc1@example.com", "password");
        User doc2 = createDoctor("doc2@example.com", "password");
        User pat = createPatient("pat1@example.com", "password");
        Room room1 = createRoom("001");
        Room room2 = createRoom("002");
        Date timeNow = this.getDateOneDayInFuture();

        Booking booking = new Booking();
        booking.setDoctor(doc1);
        booking.setPatient(pat);
        booking.setApptTime(timeNow);
        booking.setRoom(room1);

        assertTrue(
            getValidator().validate(booking).isEmpty(),
            "Booking should be valid but wasn't for reason: " + getValidator().validate(booking)
        );

        DatabaseManager.getInstance().save(booking);

        Booking booking2 = new Booking();
        booking2.setDoctor(doc2);
        booking2.setPatient(pat);
        booking2.setApptTime(timeNow);
        booking2.setRoom(room2);

        assertTrue(getValidator().validate(booking2).isEmpty());
        DatabaseManager.getInstance().save(booking2);

        assertEquals(2, DatabaseManager.getInstance().getAll(Booking.class).size());
    }
}
