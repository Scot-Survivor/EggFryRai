package com.comp5590.tests.basic.unit;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.comp5590.database.entities.Insurance;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.tests.basic.SetupTests;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class InsuranceTests extends SetupTests {

    @Test
    public void testInsuranceEntityCreation() throws ParseException {
        // Create patient object
        User testUser = SetupTests.createPatient("example@example.org", "password");

        // Create dates
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = formatDate.parse("2021-01-05");
        Date endDate = formatDate.parse("2025-03-06");

        // Create insurance object
        Insurance insurance = new Insurance();
        insurance.setUserId(testUser);
        insurance.setInsuranceProvider("Test provider");
        insurance.setStartDate(startDate);
        insurance.setEndDate(endDate);

        DatabaseManager db = DatabaseManager.getInstance();
        assertTrue(db.save(insurance));
        SetupTests.remove(testUser);
    }
}
