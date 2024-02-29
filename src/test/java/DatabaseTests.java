import com.comp5590.configuration.AppConfig;
import com.comp5590.entities.Address;
import com.comp5590.entities.Doctor;
import com.comp5590.entities.Patient;
import com.comp5590.enums.CommunicationPreference;
import com.comp5590.managers.DatabaseManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DatabaseTests {
    @BeforeAll
    public static void setup() {
        // Set up the logger
        AppConfig.ConfigFile = "src/test/resources/test.properties";
    }
    @Test
    public void testDatabaseConnection() {
        // Test database connection
        DatabaseManager db = DatabaseManager.getInstance();
        assertTrue(db.testConnection());
    }

    @Test
    public void testDoctorEntityCreation() {
        Doctor doctor = new Doctor("Test", "Test", "", "", "example@example.org",
                CommunicationPreference.EMAIL);
        DatabaseManager db = DatabaseManager.getInstance();
        assertTrue(db.save(doctor));
    }

    @Test
    public void testPatientEntityCreation() {
        Patient patient = new Patient();
        DatabaseManager db = DatabaseManager.getInstance();
        assertFalse(db.save(patient)); // This should fail as there is no address
    }

    @Test
    public void testAddressEntityCreation() {
        Address address = new Address("1234 Example St", "Test", "AB",
                "12345", "");
        DatabaseManager db = DatabaseManager.getInstance();
        assertTrue(db.save(address));
    }

    @Test
    public void testPatientWithAddress() {
        Address address = new Address("1234 Example St", "Test", "AB",
                "12345", "");
        Patient patient = new Patient();
        patient.setAddress(address);
        DatabaseManager db = DatabaseManager.getInstance();
        assertTrue(db.save(address));
        assertTrue(db.save(patient));
    }
}
