package com.comp5590.utils;

import com.comp5590.configuration.AppConfig;
import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.AuthenticationDetails;
import com.comp5590.database.entities.Booking;
import com.comp5590.database.entities.Notification;
import com.comp5590.database.entities.Prescription;
import com.comp5590.database.entities.Room;
import com.comp5590.database.entities.User;
import com.comp5590.database.entities.VisitDetails;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.enums.CommunicationPreference;
import com.comp5590.enums.UserRole;
import com.comp5590.security.managers.passwords.PasswordManager;
import java.util.Date;

public class StartupUtils {

    /**
     * This function is horrifically written as of now.
     * TODO(Josh): Move SetupTests static into a dedicated database class.
     *
     * @return true on success
     */
    public static User createObjects() {
        // initialize the database
        DatabaseManager db = DatabaseManager.getInstance();
        boolean isValid = true;
        if (db == null) {
            return null;
        }

        // create user #1
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

        // create doctor #1
        User doc1 = new User(
            "Dr Long",
            "Johnson",
            "0123456789",
            "0123456789",
            "This doctor specializes in chronic balls irritation syndrome.",
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

        // create doctor #2
        User doc2 = new User(
            "Dr Short",
            "Stack",
            "0123456789",
            "0123456789",
            "This doctor specializes in the art of psychotherapeutic ball juggling.",
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

        // create room #1
        Room room1 = new Room("1001", address);
        isValid = null != db.saveGet(room1);
        Room room2 = new Room("1002", address);
        isValid = null != db.saveGet(room2);

        // create booking #1
        Date date1 = new Date();
        date1.setTime(date1.getTime() + 5 * 24 * 60 * 60 * 1000);
        Booking booking1 = new Booking(doc1, user, date1, room1);
        isValid = null != db.saveGet(booking1);

        // create booking #2
        Date date2 = new Date();
        date2.setTime(date2.getTime() + 10 * 24 * 60 * 60 * 1000);
        Booking booking2 = new Booking(doc2, user, date2, room2);
        isValid = null != db.saveGet(booking2);

        // create booking #3
        Date date3 = new Date();
        date3.setTime(date3.getTime() + 15 * 24 * 60 * 60 * 1000);
        Booking booking3 = new Booking(doc1, user, date3, room1);
        isValid = null != db.saveGet(booking3);

        // create booking #4 (extra booking for testing)
        Date date4 = new Date();
        date4.setTime(date4.getTime() + 20 * 24 * 60 * 60 * 1000);
        Booking booking4 = new Booking(doc2, user, date4, room2);
        isValid = null != db.saveGet(booking4);

        // create booking #5 (extra booking for testing)
        Date date5 = new Date();
        date5.setTime(date5.getTime() + 21 * 24 * 60 * 60 * 1000);
        Booking booking5 = new Booking(doc1, user, date5, room1);
        isValid = null != db.saveGet(booking5);

        // create visitDetails #1, link visit details to booking1
        VisitDetails visitDetails1 = new VisitDetails(
            true,
            "Patient had chronic cough, fever, and fatigue.",
            "Common cold",
            "Get plenty of rest, drink fluids, and take over-the-counter medications.",
            new Date(booking1.getApptTime().getTime() + 60 * 60 * 1000), // 1 hour after appt
            booking1
        );
        isValid = null != db.saveGet(visitDetails1);

        // create visitDetails #2, link visit details to booking2
        VisitDetails visitDetails2 = new VisitDetails(
            false,
            "Patient had brain damage from a severe head injury following a car accident.",
            "Severe internal haemorrhaging and unexpectedly AIDS syndrome stage 8",
            "Give the patient a lollipop and send them on their way. Patient is dead now. RIP.",
            new Date(booking2.getApptTime().getTime() + 60 * 60 * 1000), // 1 hour after appt
            booking2
        );
        isValid = null != db.saveGet(visitDetails2);

        // create visitDetails #3, link visit details to booking3
        VisitDetails visitDetails3 = new VisitDetails(
            false,
            "Patient had a severe allergic reaction to peanuts.",
            "Anaphylaxis",
            "Administered epinephrine and sent to the hospital.",
            new Date(booking3.getApptTime().getTime() + 60 * 60 * 1000), // 1 hour after appt
            booking3
        );
        isValid = null != db.saveGet(visitDetails3);

        // create prescription #1, link prescription to visitDetails #1
        Prescription prescription1 = new Prescription("Tylenol", "3 pill every 28 hours", visitDetails1);
        isValid = null != db.saveGet(prescription1);

        // create prescription #2, link prescription to visitDetails #1
        Prescription prescription2 = new Prescription("Advil", "1 pill every 6 hours", visitDetails2);
        isValid = null != db.saveGet(prescription2);

        // create prescription #3, link prescription to visitDetails #2
        Prescription prescription3 = new Prescription("Morphine", "2 pills every 8 hours", visitDetails2);
        isValid = null != db.saveGet(prescription3);

        // create prescription #4, link prescription to visitDetails #3
        Prescription prescription4 = new Prescription("Epinephrine", "1 shot", visitDetails3);
        isValid = null != db.saveGet(prescription4);

        // create a bunch of notifications
        Notification notification1 = new Notification("You have a new booking with Dr Long Johnson.", false, user);
        isValid = null != db.saveGet(notification1);
        Notification notification2 = new Notification("You have a new booking with Dr Short Stack.", false, user);
        isValid = null != db.saveGet(notification2);
        Notification notification3 = new Notification("You have a new booking with Dr Long Johnson.", false, user);
        isValid = null != db.saveGet(notification3);
        Notification notification4 = new Notification("You have a new booking with Dr Short Stack.", false, user);
        isValid = null != db.saveGet(notification4);
        Notification notification5 = new Notification("You have a new booking with Dr Long Johnson.", false, user);
        isValid = null != db.saveGet(notification5);
        Notification notification6 = new Notification("You have a new booking with Dr Short Stack.", false, user);
        isValid = null != db.saveGet(notification6);
        Notification notification7 = new Notification("You have a new booking with Dr Long Johnson.", false, user);
        isValid = null != db.saveGet(notification7);

        return user;
    }
}
