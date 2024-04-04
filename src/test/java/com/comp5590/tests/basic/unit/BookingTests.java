package com.comp5590.tests.basic.unit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.comp5590.database.entities.*;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.enums.UserRole;
import com.comp5590.tests.basic.SetupTests;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class BookingTests extends SetupTests {

    /**
     * Create a booking entity given a roomNum
     * @param roomNum The room number to use
     * @return A Booking entity
     */
    private Booking makeBooking(String roomNum) {
        // create a db manager
        DatabaseManager db = DatabaseManager.getInstance();

        // create user entity
        User patient = createPatient("example@example.com", "password");

        // create doctor entity
        User doctor = createPatient("example1@example.com", "password");
        doctor.setRole(UserRole.DOCTOR);

        // Use current time plus two weeks to make test more valid for future
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime futureDateTime = currentTime.plus(2, ChronoUnit.WEEKS);
        Date twoWeeks = java.sql.Timestamp.valueOf(futureDateTime);

        // make and save room
        Room room = makeRoom(roomNum, createAddress());
        assertNotNull(room);

        // create booking object
        Booking booking = new Booking();
        booking.setDoctor(doctor);
        booking.setPatient(patient);
        booking.setApptTime(twoWeeks);
        booking.setRoom(room);

        return booking;
    }

    /**
     * Test the creation of a booking entity on its own
     */
    @Test
    public void testBookingEntityCreation() {
        DatabaseManager db = DatabaseManager.getInstance();

        // check saving of booking was successfully
        assertTrue(db.save(makeBooking("1")));
    }
}
