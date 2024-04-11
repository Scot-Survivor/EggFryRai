package com.comp5590.utils;

import com.comp5590.configuration.AppConfig;
import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.AuthenticationDetails;
import com.comp5590.database.entities.Medicine;
import com.comp5590.database.entities.Prescription;
import com.comp5590.database.entities.Room;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.enums.CommunicationPreference;
import com.comp5590.enums.UserRole;
import com.comp5590.security.managers.passwords.PasswordManager;

public class StartupUtils {

    /**
     * This function is horrifically written as of now.
     * TODO(Josh): Move SetupTests static into a dedicated database class.
     * @return true on success
     */
    public static User createObjects() {
        DatabaseManager db = DatabaseManager.getInstance();
        boolean isValid = true;
        if (db == null) {
            return null;
        }

        Address address = new Address("1234 Test St", "Test City", "Test Province", "Test Country", "T3S T1N");
        address = db.saveGet(address);
        isValid = null != address;
        User user = new User(
            "Test",
            "User",
            "0123456789",
            "0123456789",
            "",
            CommunicationPreference.EMAIL,
            UserRole.PATIENT,
            address
        );

        AuthenticationDetails auth = new AuthenticationDetails(
            "testuser@example.com",
            PasswordManager.getInstanceOf(AppConfig.HASH_ALGORITHM).hashPassword("password"),
            false,
            null,
            null
        );
        auth = db.saveGet(auth);
        isValid = null != auth;
        user.setAuthenticationDetails(auth);
        isValid = null != db.saveGet(user);

        User doc1 = new User(
            "Test",
            "Doctor",
            "0123456789",
            "0123456789",
            "",
            CommunicationPreference.EMAIL,
            UserRole.DOCTOR,
            address
        );
        auth =
        new AuthenticationDetails(
            "testdoc1@example.com",
            PasswordManager.getInstanceOf(AppConfig.HASH_ALGORITHM).hashPassword("password"),
            false,
            null,
            null
        );
        auth = db.saveGet(auth);
        isValid = null != auth;
        doc1.setAuthenticationDetails(auth);
        isValid = null != db.saveGet(doc1);
        User doc2 = new User(
            "Test",
            "Doctor2",
            "0123456789",
            "0123456789",
            "",
            CommunicationPreference.EMAIL,
            UserRole.DOCTOR,
            address
        );
        auth =
        new AuthenticationDetails(
            "testdoc2@example.com",
            PasswordManager.getInstanceOf(AppConfig.HASH_ALGORITHM).hashPassword("password"),
            false,
            null,
            null
        );
        auth = db.saveGet(auth);
        isValid = null != auth;
        doc2.setAuthenticationDetails(auth);
        isValid = null != db.saveGet(doc2);

        address = new Address("5678 Test St", "Test City", "Test Province", "Test Country", "T3S T1N");
        address = db.saveGet(address);
        isValid = null != address;

        Room room = new Room("1001", address);
        isValid = null != db.saveGet(room);
        room = new Room("1002", address);
        isValid = null != db.saveGet(room);
        
        Prescription prescription = new Prescription();
        prescription.setAdditionalNotes("Prescription details here");
        prescription = db.saveGet(prescription);
        if (prescription == null) {
            isValid = false;
        }

        Medicine medicine1 = new Medicine("Medicine 1 Name", 1, prescription);
        Medicine medicine2 = new Medicine("Medicine 2 Name", 2, prescription);
        isValid &= db.saveGet(medicine1) != null;
        isValid &= db.saveGet(medicine2) != null;

        return user;
    }
}
