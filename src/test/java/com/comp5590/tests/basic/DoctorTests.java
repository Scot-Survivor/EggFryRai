package com.comp5590.tests.basic;

import com.comp5590.entities.Doctor;
import com.comp5590.enums.CommunicationPreference;
import com.comp5590.managers.DatabaseManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DoctorTests extends SetupTests {
    @Test
    public void testDoctorEntityCreation() {
        Doctor doctor = new Doctor("Test", "Test", "", "", "example@example.org",
                CommunicationPreference.EMAIL);
        DatabaseManager db = DatabaseManager.getInstance();
        assertTrue(db.save(doctor));
    }
}
