package com.comp5590.tests.basic.unit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.Booking;
import com.comp5590.database.entities.Prescription;
import com.comp5590.database.entities.Room;
import com.comp5590.database.entities.User;
import com.comp5590.database.entities.VisitDetails;
import com.comp5590.tests.basic.SetupTests;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

public class VisitDetailsTests extends SetupTests {

    private final User patient = createPatient("test1@test.com", "password");
    private final User doctor = createDoctor("test2@test.com", "password");
    private final Address address = createAddress();
    private final Room room = createRoom("1", address);

    private Booking createBooking() {
        Date date = createFutureDate(2);
        Booking booking = createBooking(patient, doctor, room, date);
        return booking;
    }

    private VisitDetails createViewDetails(Booking booking) {
        VisitDetails visitDetails = new VisitDetails(
            true,
            "Patient complains about being under 6' tall",
            "Diagnosis: skill issue",
            "Advice: get good",
            new Date(),
            booking
        );
        return visitDetails;
    }

    private Prescription createPrescription(VisitDetails visitDetails) {
        Prescription prescription = new Prescription("Tylenol", "1 pill every 4 hours", visitDetails);
        return prescription;
    }

    @Test
    public void testVisitDetailsCreationNotNull() {
        // make booking
        Booking booking = createBooking();

        // make visit details
        VisitDetails visitDetails = createViewDetails(booking);

        // check that visit details is not null
        assertNotNull(visitDetails);
    }

    @Test
    public void testVisitDetailsCreationWithPrescriptionsNotNull() {
        // make booking
        Booking booking = createBooking();

        // check that booking is not null
        assertNotNull(booking);

        // make visit details
        VisitDetails visitDetails = createViewDetails(booking);

        // check that visit details is not null
        assertNotNull(visitDetails);

        // create 1 - 10 prescriptions at random
        List<Prescription> prescriptions = new ArrayList<>();
        for (int i = 0; i < (int) (Math.random() * 10) + 1; i++) {
            // create prescription
            Prescription prescription = createPrescription(visitDetails);

            // check that prescription is not null
            assertNotNull(prescription);

            // add prescription to to prescriptions list
            prescriptions.add(prescription);
        }

        // add prescriptions to visit details
        visitDetails.setPrescriptions(prescriptions);

        // check that visit details has prescriptions (assert that the list is not null
        // nor empty)
        assertNotNull(visitDetails.getPrescriptions());
        assertTrue(visitDetails.getPrescriptions().size() > 0);
    }
}
