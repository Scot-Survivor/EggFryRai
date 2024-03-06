package com.comp5590.tests.basic;

import com.comp5590.App;
import com.comp5590.configuration.AppConfig;
import com.comp5590.entities.Address;
import com.comp5590.entities.Patient;
import com.comp5590.enums.CommunicationPreference;
import com.comp5590.managers.DatabaseManager;
import org.junit.jupiter.api.BeforeAll;

public class SetupTests {
    @BeforeAll
    public static void setup() {
        // Set up the logger
        AppConfig.ConfigFile = "src/test/resources/test.properties";
    }

    // QOL Of Methods
    public static DatabaseManager getDbManager() {
        return DatabaseManager.getInstance();
    }

    /**
     * Create an address object with default values.
     * @return Address
     */
    public static Address createAddress() {
        Address address = new Address();
        address.setPostCode("CT1 1AA");
        address.setCountry("GB");
        address.setAddressLineOne("1 Test Street");
        address.setAddressLineTwo("Test Town");
        address.setAddressLineThree("Test County");
        int id = getDbManager().saveGetId(address);
        address = getDbManager().get(Address.class, id);
        return address;
    }

    /**
     * Create a patient user with all filled values,
     * @return Patient
     */
    public static Patient createPatient() {
        Address address = createAddress();
        Patient patient = new Patient("Test", "User", "0123456789", "0123456789",
                "Test Notes", CommunicationPreference.EMAIL, "example@example.org",
                "password", false, null, null, address);
        int id = getDbManager().saveGetId(patient);
        patient = getDbManager().get(Patient.class, id);
        return patient;
    }

    /**
     * Create a user object with the email and password specified.
     * @param email the email of the user
     * @param password the password of the user
     * @return User
     */
    public static Patient createPatient(String email, String password) {
        Patient patient = createPatient();
        patient.setEmail(email);
        patient.setPassword(App.getInstance().getPasswordManager().hashPassword(password));
        // update
        getDbManager().update(patient);
        patient = getDbManager().get(Patient.class, patient.getId());
        return patient;
    }

    /**
     * Create a user object with the email and password specified.
     * @param email the email of the user
     * @param password the password of the user
     * @param twoFactorEnabled if two factor authentication is enabled
     * @param authenticationToken the authentication token
     * @param recoveryCodes the recovery codes
     * @return User
     */
    public static Patient createPatient(String email, String password, boolean twoFactorEnabled, String authenticationToken, String recoveryCodes) {
        Patient patient = createPatient(email, password);
        patient.setTwoFactorEnabled(twoFactorEnabled);
        patient.setAuthenticationToken(authenticationToken);
        patient.setRecoveryCodes(recoveryCodes);
        // update
        getDbManager().update(patient);
        patient = getDbManager().get(Patient.class, patient.getId());
        return patient;
    }

    /**
     * Remove an object from the database by id
     */
    public static <T> void remove(final Class<T> type, int id) {
        getDbManager().delete(getDbManager().get(type, id));
    }
}
