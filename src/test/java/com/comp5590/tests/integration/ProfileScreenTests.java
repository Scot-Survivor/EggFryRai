package com.comp5590.tests.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.comp5590.App;
import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.Booking;
import com.comp5590.database.entities.Room;
import com.comp5590.database.entities.User;
import com.comp5590.database.entities.VisitDetails;
import com.comp5590.screens.ProfileScreen;
import com.comp5590.tests.basic.SetupTests;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class ProfileScreenTests extends SetupTests {

    App app;
    private final String email = "patient1@gmail.com";
    private final String password = "password";

    @Start
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
        stage.show();
        setupDB();
    }
    private void setupDB() {
        // create user
        User user = createPatient(email, password);

        this.setAuthenticatedUser(app, user);

        // create doctor
        User doc = createDoctor("doctor67", "password");

        // create address
        Address address = createAddress();

        // create room
        Room room1 = createRoom("room1", address);
        Room room2 = createRoom("room2", address);
        Room room3 = createRoom("room3", address);

        // create bookings
        Booking booking1 = createBooking(user, doc, room1, createFutureDate(2));
        Booking booking2 = createBooking(user, doc, room2, createFutureDate(3));
        Booking booking3 = createBooking(user, doc, room3, createFutureDate(4));

        // create visit details
        VisitDetails visitDetails1 = createVisitDetails(
            true,
            "complaint",
            "diagnosis",
            "advice",
            createFutureDate(2),
            booking1
        );
        VisitDetails visitDetails2 = createVisitDetails(
            true,
            "complaint",
            "diagnosis",
            "advice",
            createFutureDate(3),
            booking2
        );
        VisitDetails visitDetails3 = createVisitDetails(
            true,
            "complaint",
            "diagnosis",
            "advice",
            createFutureDate(4),
            booking3
        );

        // create prescriptions
        createPrescription("medication", "dosage", visitDetails1);
        createPrescription("medication", "dosage", visitDetails2);
        createPrescription("medication", "dosage", visitDetails3);
    }

    @Test
    public void testEmailChange(FxRobot robot) {
        // log in with a test user
        loginUser(app, robot, email,password);

        // go to the profile screen
        goToScreenWithAutoAuthentication(app, robot, ProfileScreen.class);

        // input the new email
        String newEmail = "newemail@example.com";
        robot.lookup("#newEmailField").queryAs(TextField.class).setText(newEmail);

        // click the apply email button
        robot.clickOn("#applyEmailButton");

        //  the updated user from the database
        User updatedUser = getDbManager().getByProperty(User.class, "authenticationDetails.email", newEmail);

        assertNotNull(updatedUser);
        assertEquals(newEmail, updatedUser.getAuthenticationDetails().getEmail());
    }


}
