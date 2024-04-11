package com.comp5590.tests.basic.unit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.comp5590.database.entities.Prescription;
import com.comp5590.database.entities.User;
import com.comp5590.tests.basic.SetupTests;
import org.junit.jupiter.api.Test;

public class PrescriptionTests extends SetupTests {

    @Test
    public void testPrescriptionEntityCreation() {
        User patient = createPatient("test@test.com", "password");
        User doctor = createDoctor("test1@test.com", "password");
        Prescription prescription = createPrescription("none", patient, doctor);
        assertNotNull(prescription);
    }
}
