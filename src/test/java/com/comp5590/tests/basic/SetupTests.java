package com.comp5590.tests.basic;

import com.comp5590.configuration.AppConfig;
import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.AuthenticationDetails;
import com.comp5590.database.entities.Room;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.enums.CommunicationPreference;
import com.comp5590.enums.UserRole;
import com.comp5590.security.managers.passwords.PasswordManager;
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
     * Create authentication details without mfa
     * @return AuthenticationDetails
     */
    public static AuthenticationDetails createAuthenticationDetails(String email, String password) {
        AuthenticationDetails authenticationDetails = new AuthenticationDetails();
        authenticationDetails.setEmail(email);
        authenticationDetails.setPassword(PasswordManager.getInstanceOf("Argon2").hashPassword(password));
        authenticationDetails.setTwoFactorEnabled(false);
        authenticationDetails.setAuthenticationToken("");
        authenticationDetails.setRecoveryCodes("");
        int id = getDbManager().saveGetId(authenticationDetails);
        authenticationDetails = getDbManager().get(AuthenticationDetails.class, id);
        return authenticationDetails;
    }

    /**
     * Create authentication details with mfa
     * @return AuthenticationDetails
     */
    public static AuthenticationDetails createAuthenticationDetails(
        String email,
        String password,
        String authenticationToken,
        String recoveryCodes
    ) {
        AuthenticationDetails authenticationDetails = createAuthenticationDetails(email, password);
        authenticationDetails.setTwoFactorEnabled(true);
        authenticationDetails.setAuthenticationToken(authenticationToken);
        authenticationDetails.setRecoveryCodes(recoveryCodes);
        // update
        getDbManager().update(authenticationDetails);
        authenticationDetails = getDbManager().get(AuthenticationDetails.class, authenticationDetails.getId());
        return authenticationDetails;
    }

    /**
     * Create a patient user with all filled values,
     * @return User
     */
    public static User createPatient(AuthenticationDetails authenticationDetails) {
        Address address = createAddress();
        User user = new User(
            "Test",
            "User",
            "0123456789",
            "0123456789",
            "Test Notes",
            CommunicationPreference.EMAIL,
            address
        );
        user.setRole(UserRole.PATIENT);
        user.setAuthenticationDetails(authenticationDetails);
        int id = getDbManager().saveGetId(user);
        user = getDbManager().get(User.class, id);
        return user;
    }

    /**
     * Create a user object with the email and password specified.
     * @param email the email of the user
     * @param password the password of the user
     * @return User
     */
    public static User createPatient(String email, String password) {
        return createPatient(createAuthenticationDetails(email, password));
    }

    /**
     * Create a user object with the email and password specified.
     * @param email the email of the user
     * @param password the password of the user
     * @param authenticationToken the authentication token
     * @param recoveryCodes the recovery codes
     * @return User
     */
    public static User createPatient(String email, String password, String authenticationToken, String recoveryCodes) {
        return createPatient(createAuthenticationDetails(email, password, authenticationToken, recoveryCodes));
    }

    /**
     * Create a Room object using roomNum and address
     * @param roomNum The number of the room
     * @param address The address of the room
     * @return The room
     */
    public Room makeRoom(String roomNum, Address address) {
        Room room = new Room();
        room.setAddress(address);
        room.setRoomNumber(roomNum);
        return room;
    }

    /**
     * Remove an object from the database by id
     */
    public static <T> void remove(final Class<T> type, int id) {
        getDbManager().delete(getDbManager().get(type, id));
    }
}
