package com.comp5590.tests.basic;

import com.comp5590.database.entities.Booking;
import com.comp5590.database.entities.BookingNotes;
import com.comp5590.database.entities.Room;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.enums.UserRole;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingNotesTests extends SetupTests {

    @Test
    public void testBookingNotesEntityCreation() throws ParseException {
        // Create a db manager
        DatabaseManager db = DatabaseManager.getInstance();

        // Create a patient user entity
        User patient = createPatient("test2@gmail.com", "password");

        // Create a doctor user entity
        User doctor = createPatient("test3@gmail.com", "password");
        doctor.setRole(UserRole.DOCTOR);

        // Create a date and time for the appointment
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date apptTime = formatDate.parse("2025-01-05 00:00:00");

        // Make and save a room
        Room room = makeRoom("1", createAddress());
        db.save(room);

        // Create a booking object
        Booking booking = new Booking();
        booking.setDoctor(doctor);
        booking.setPatient(patient);
        booking.setApptTime(apptTime);
        booking.setRoom(room);
        db.save(booking);

        // Create booking notes
        BookingNotes bookingNotes = new BookingNotes();
        bookingNotes.setBookingId(booking);
        bookingNotes.setNotes("Test notes");
        bookingNotes.setPrescription("Test prescription");

        // Check saving of booking notes was successful
        assertTrue(db.save(bookingNotes));
    }
}
