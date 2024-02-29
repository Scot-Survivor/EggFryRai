import com.comp5590.configuration.AppConfig;
import com.comp5590.entities.Doctor;
import com.comp5590.enums.CommunicationPreference;
import com.comp5590.managers.DatabaseManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
    public void testDoctorEntity() {
        Doctor doctor = new Doctor("Test", "Test", "", "", "example@example.org",
                CommunicationPreference.EMAIL);
        DatabaseManager db = DatabaseManager.getInstance();
        assertTrue(db.save(doctor));
    }
}
