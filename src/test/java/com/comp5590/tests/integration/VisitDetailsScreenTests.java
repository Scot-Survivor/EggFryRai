package com.comp5590.tests.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.comp5590.App;
import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.Booking;
import com.comp5590.database.entities.Room;
import com.comp5590.database.entities.User;
import com.comp5590.database.entities.VisitDetails;
import com.comp5590.screens.VisitDetailsScreen;
import com.comp5590.tests.basic.SetupTests;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class VisitDetailsScreenTests extends SetupTests {

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

    /**
     * Put mock data in the database
     */
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
    public void testVisitDetailsScreenCanBeAccessed(FxRobot robot) {
        this.goToScreen(app, robot, VisitDetailsScreen.class);

        assertTrue(app.getScreenManager().getCurrentScreen().getClass() == VisitDetailsScreen.class);
    }

    @Test
    public void testVisitDetailsScreenDisplaysVisitDetails(FxRobot robot) {
        this.goToScreen(app, robot, VisitDetailsScreen.class);

        robot.interact(() -> {
            VBox scrollerBox = robot.lookup("#scroller-box").queryAs(VBox.class);

            // assert that the scroller box is visible
            assertTrue(scrollerBox.isVisible());

            // assert that the scroller box has children
            assertTrue(scrollerBox.getChildren().size() > 0);
        });
    }

    @Test
    public void testVisitDetailsScreenShowsCorrentNumberOfVisitDetails(FxRobot robot) {
        this.goToScreen(app, robot, VisitDetailsScreen.class);

        // grab ScrollerBox
        robot.interact(() -> {
            VBox scrollerBox = robot.lookup("#scroller-box").queryAs(VBox.class);

            // check if the number of visit details displayed is equal to the number of
            // visit details in the database
            assertTrue(scrollerBox.getChildren().size() == app.getDatabase().getAll(VisitDetails.class).size());
        });
    }

    @Test
    public void testVisitDetailsScreenDisplaysNoVisitDetailsMessageIfNoneExist(FxRobot robot) {
        // reset database entirely by deleting all entities
        this.resetDatabase();

        this.goToScreen(app, robot, VisitDetailsScreen.class);

        robot.interact(() -> {
            // grab the no visit details label
            HBox noVisitDetailsLabel = robot.lookup("#no-visit-details-label").queryAs(HBox.class);

            // assert that the no visit details label is not null & visible
            assertTrue(noVisitDetailsLabel != null && noVisitDetailsLabel.isVisible());
        });
    }
}
