package com.comp5590.tests.basic.unit;

import static org.junit.jupiter.api.Assertions.*;

import com.comp5590.database.entities.*;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.tests.basic.SetupTests;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

public class BookingTests extends SetupTests {

    private final User patient = createPatient("test1@test.com", "password");
    private final User doctor = createDoctor("test2@test.com", "password");
    private final Address address = createAddress();
    private final Room room = createRoom("1", address);

    /**
     * Test the creation of a booking entity on its own
     */
    @Test
    public void testBookingEntityCreation() {
        Date date = createFutureDate(2);
        Booking booking = createBooking(patient, doctor, room, date);
        // check saving of booking was successfully
        assertNotNull(booking);
    }

    /**
     * Test making two bookings
     */
    @Test
    public void testTwoBookings() {
        Date date = createFutureDate(2);
        Date longerDate = createFutureDate(3);
        createBooking(patient, doctor, room, date);
        createBooking(patient, doctor, room, longerDate);

        DatabaseManager db = DatabaseManager.getInstance();

        List<Booking> bookings = db.getAll(Booking.class);
        assertEquals(2, bookings.size());
    }
}
