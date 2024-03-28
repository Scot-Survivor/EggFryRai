package com.comp5590.utils;

import com.comp5590.configuration.AppConfig;
import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.AuthenticationDetails;
import com.comp5590.database.entities.Room;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.enums.CommunicationPreference;
import com.comp5590.enums.UserRole;
import com.comp5590.security.managers.passwords.PasswordManager;

public class StartupUtils {

    public static boolean createObjects() {
        DatabaseManager db = DatabaseManager.getInstance();

        Address address = createAddress();
        User user = createUser(address, createAuth("testuser@test.com", "password"));
        createDoctor(address, createAuth("testdoc1@test.com", "password"));
        createDoctor(address, createAuth("testdoc2@test.com", "password"));
        createRoom(address, "1000");
        createRoom(address, "1001");

        return db.get(User.class, user.getId()) != null;
    }

    private static void createRoom(Address address, String roomNum) {
        DatabaseManager db = DatabaseManager.getInstance();
        Room room = new Room();
        room.setRoomNumber(roomNum);
        room.setAddress(address);
        db.save(room);
    }

    /**
     * Create a default doctor
     * @param address Debug address object
     * @param auth Auth details for debug doctor object
     */
    private static void createDoctor(Address address, AuthenticationDetails auth) {
        DatabaseManager db = DatabaseManager.getInstance();

        User doctor = new User(
            "Testing",
            "Test",
            "1234567890",
            "0987654321",
            "This is a test user",
            CommunicationPreference.EMAIL,
            UserRole.DOCTOR,
            address
        );
        doctor.setAuthenticationDetails(auth);
        doctor = db.saveGet(doctor);
    }

    private static AuthenticationDetails createAuth(String email, String password) {
        DatabaseManager db = DatabaseManager.getInstance();

        AuthenticationDetails auth = new AuthenticationDetails(
            email,
            PasswordManager.getInstanceOf(AppConfig.HASH_ALGORITHM).hashPassword(password),
            false,
            null,
            null
        );

        return auth;
    }

    /**
     * Create a default user
     * @param address Address of user
     * @return A User object
     */
    private static User createUser(Address address, AuthenticationDetails auth) {
        DatabaseManager db = DatabaseManager.getInstance();

        User user = new User(
            "Testing",
            "Test",
            "1234567890",
            "0987654321",
            "This is a test user",
            CommunicationPreference.EMAIL,
            UserRole.PATIENT,
            address
        );
        user.setAuthenticationDetails(auth);
        user = db.saveGet(user);

        return user;
    }

    /**
     * Create a default address
     * @return Address
     */
    private static Address createAddress() {
        DatabaseManager db = DatabaseManager.getInstance();
        Address address = new Address(
            "1234 Example St",
            "Example City",
            "Example County",
            "Example Country",
            "A1A 1A1"
        );
        address = db.saveGet(address);
        return address;
    }
}
