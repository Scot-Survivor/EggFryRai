package com.comp5590.tests.basic.unit;

import static org.junit.jupiter.api.Assertions.*;

import com.comp5590.database.entities.*;
import com.comp5590.tests.basic.SetupTests;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class BookingTests extends SetupTests {

    /**
     * Test the creation of a booking entity on its own
     */
    @Test
    public void testBookingEntityCreation() {
        User patient = createPatient("test1@test.com", "password");
        User doctor = createDoctor("test2@test.com", "password");
        Date date = createFutureDate(2);
        Address address = createAddress();
        Room room = makeRoom("1", address);

        Booking booking = makeBooking(patient, doctor, room, date);

        // check saving of booking was successfully
        assertInstanceOf(Booking.class, booking);
    }
}
