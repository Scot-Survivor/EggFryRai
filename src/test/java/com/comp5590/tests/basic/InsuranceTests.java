package com.comp5590.tests.basic;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.Insurance;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class InsuranceTests extends SetupTests {

    @Test
    public void testInsuranceEntityCreation() throws ParseException {
        // Make address for patient object
        Address address = new Address("1234 Example St", "Test", "AB", "12345", "");

        // Create patient object
        User testUser = new User();
        testUser.setAddress(address);

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
    }
}
