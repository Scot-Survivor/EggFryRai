package com.comp5590.tests.basic.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.comp5590.database.entities.Medicine;
import com.comp5590.database.entities.Prescription;
import com.comp5590.database.entities.User;
import com.comp5590.tests.basic.SetupTests;
import org.junit.jupiter.api.Test;

public class MedicineTests extends SetupTests {

    private final User patient = createPatient("test@test.com", "password");
    private final User doctor = createDoctor("test1@test.com", "password");
    private final Prescription prescription = createPrescription("none", patient, doctor);

    /**
     * Test creating one prescription
     */
    @Test
    public void testMedicineEntityCreation() {
        Medicine med = createMedicine("Paracetamol", 3, prescription);
        assertNotNull(med);
    }

    /**
     * Test creating two of the same name. Should fail on second
     */
    @Test
    public void testDoubleCreationSame() {
        Medicine med = createMedicine("Paracetamol", 3, prescription);
        Medicine secondMedicine = createMedicine("Paracetamol", 3, prescription);
        assertEquals(true, getDbManager().getAll(Medicine.class).size() == 1);
    }
}
