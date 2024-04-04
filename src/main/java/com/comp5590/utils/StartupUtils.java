package com.comp5590.utils;

import com.comp5590.configuration.AppConfig;
import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.AuthenticationDetails;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.enums.CommunicationPreference;
import com.comp5590.enums.UserRole;
import com.comp5590.security.managers.passwords.PasswordManager;

public class StartupUtils {

    public static boolean createObjects() {
        DatabaseManager db = DatabaseManager.getInstance();
        if (db == null) {
            return false;
        }
        AuthenticationDetails auth = new AuthenticationDetails(
            "example@example.com",
            PasswordManager.getInstanceOf(AppConfig.HASH_ALGORITHM).hashPassword("password"),
            false,
            null,
            null
        );
        Address address = new Address(
            "1234 Example St",
            "Example City",
            "Example County",
            "Example Country",
            "A1A 1A1"
        );
        auth = db.saveGet(auth);
        address = db.saveGet(address);
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
        return db.get(User.class, user.getId()) != null;
    }
}
