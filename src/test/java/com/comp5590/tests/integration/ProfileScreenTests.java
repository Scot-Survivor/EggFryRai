package com.comp5590.tests.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.comp5590.App;
import com.comp5590.configuration.AppConfig;
import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.Booking;
import com.comp5590.database.entities.Room;
import com.comp5590.database.entities.User;
import com.comp5590.database.entities.VisitDetails;
import com.comp5590.events.eventtypes.users.UserUpdateEvent;
import com.comp5590.events.listeners.interfaces.UserListener;
import com.comp5590.events.managers.EventManager;
import com.comp5590.managers.LoggerManager;
import com.comp5590.screens.general.settings.ProfileScreen;
import com.comp5590.security.managers.passwords.PasswordManager;
import com.comp5590.tests.basic.SetupTests;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.logging.log4j.core.Logger;
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
    private final PasswordManager passwordManager = PasswordManager.getInstanceOf(AppConfig.HASH_ALGORITHM);

    @Start
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
        stage.show();
        setupDB();
    }

    @Getter
    public static class TestListener implements UserListener {

        private boolean isCalled = false;
        private final Logger logger = LoggerManager.getInstance().getLogger(TestListener.class, "DEBUG");

        @Override
        public UserUpdateEvent onUserUpdate(UserUpdateEvent event) {
            isCalled = true;
            logger.debug("User update event called");
            return event;
        }
    }

    private TestListener setUpListeners() {
        TestListener listener = new TestListener();
        EventManager.getInstance().addListener(listener);
        return listener;
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
        loginUser(app, robot, email, password);

        // go to the profile screen
        goToScreenWithAutoAuthentication(app, robot, ProfileScreen.class);

        robot.interact(() -> {
            TestListener listener = setUpListeners();

            // input the new email
            String newEmail = "newemail@example.com";
            robot.lookup("#newEmailField").queryAs(TextField.class).setText(newEmail);

            // click the apply email button
            robot.lookup("#applyEmailButton").queryAs(Button.class).fire();

            // get the updated user from the database
            User updatedUser = getDbManager().getByProperty(User.class, "authenticationDetails.email", newEmail);

            assertNotNull(updatedUser);
            assertEquals(newEmail, updatedUser.getAuthenticationDetails().getEmail());
            assertTrue(listener.isCalled);
        });
    }

    @Test
    public void testPasswordChange(FxRobot robot) {
        // log in with a test user
        loginUser(app, robot, email, password);

        // go to the profile screen
        goToScreenWithAuthentication(app, robot, ProfileScreen.class, email, password);

        robot.interact(() -> {
            TestListener listener = setUpListeners();
            // input the new password
            String newPassword = "newpassword";
            robot.lookup("#newPasswordField").queryAs(TextField.class).setText(newPassword);

            // click the apply password button
            robot.lookup("#applyPasswordButton").queryAs(Button.class).fire();
            stall(robot);
            // get the updated user from the database
            User updatedUser = getDbManager().getByProperty(User.class, "authenticationDetails.email", email);

            assertNotNull(updatedUser);
            logout(app, robot);
            stall(robot);
            goToScreenWithAuthentication(app, robot, ProfileScreen.class, email, newPassword);
            stall(robot);
            assertEquals(app.getSessionManager().getCurrentUser().getId(), updatedUser.getId());
            assertTrue(listener.isCalled);
        });
    }
}
