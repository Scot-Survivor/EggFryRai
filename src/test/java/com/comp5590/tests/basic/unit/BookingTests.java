package com.comp5590.tests.basic.unit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.comp5590.database.entities.*;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.enums.UserRole;
import com.comp5590.tests.basic.SetupTests;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class BookingTests extends SetupTests {

    @Test
    public void testBookingEntityCreation() throws ParseException {
        // create a db manager
        DatabaseManager db = DatabaseManager.getInstance();

        // create user entity
        User patient = createPatient("example@example.com", "password");

        // create doctor entity
        User doctor = createPatient("example1@example.com", "password");
        doctor.setRole(UserRole.DOCTOR);

        // create a date and time for the appt
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date apptTime = formatDate.parse("2025-01-05 00:00:00");

        // make and save room
        Room room = makeRoom("1", createAddress());
        assertNotNull(room);

        // create booking object
        Booking booking = new Booking();
        booking.setDoctor(doctor);
        booking.setPatient(patient);
        booking.setApptTime(apptTime);
        booking.setRoom(room);

        // check saving of booking was successfully
        assertTrue(db.save(booking));
        SetupTests.remove(patient);
        SetupTests.remove(doctor);
    }
}
