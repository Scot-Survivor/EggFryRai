package com.comp5590.tests.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.comp5590.App;
import com.comp5590.components.CreateBooking.WarningMessage;
import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.Booking;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.CreateBooking;
import com.comp5590.tests.basic.SetupTests;
import java.time.LocalDate;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class CreateBookingTests extends SetupTests {

    private final String APPOINTMENT_REASON = "Lumpy Balls";
    private final LocalDate APPOINTMENT_TIME = LocalDate.now().plusWeeks(2);
    App app;

    @Start
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
        stage.show();
        setupDB();
    }

    /**
     * Create some mock data to put in the db
     */
    private void setupDB() {
        // Make an address
        Address address = createAddress();

        // Create some rooms
        makeRoom("1000", address);
        makeRoom("1001", address);
        makeRoom("1002", address);

        // Make some doctors
        createDoctor("testEmail1000@test.com", "testPassword");
        createDoctor("testEmail1001@test.com", "testPassword");
        createDoctor("testEmail1002@test.com", "testPassword");
    }

    /**
     * Create a booking for a given time
     * @param robot FxRobot object
     * @param time Index of the time to pick from dropdown
     */
    private void createBooking(FxRobot robot, int time) {
        robot.interact(() -> {
            app.getScreenManager().showScene(CreateBooking.class);
            robot.lookup("#doctorChoiceBox").queryAs(ChoiceBox.class).getSelectionModel().select(0); // Just select first doctor
            robot.lookup("#roomChoiceBox").queryAs(ChoiceBox.class).getSelectionModel().select(0); // just select first room
            robot.lookup("#time").queryAs(ChoiceBox.class).getSelectionModel().select(time); // just select first time
            robot.lookup("#apptReasonTextField").queryAs(TextField.class).setText(APPOINTMENT_REASON);
            robot.lookup("#datePicker").queryAs(DatePicker.class).setValue(APPOINTMENT_TIME);
            robot.lookup("#bookingButton").queryAs(javafx.scene.control.Button.class).fire();
            stall(robot);
        });
    }

    /**
     * Test filling in the form and booking an appointment
     * @param robot
     */
    @Test
    public void testBookingCreation(FxRobot robot) {
        // goToScreen(app, robot, CreateBooking.class);
        // We have to ensure we're a patient logged in
        User user = createPatient("testPatient1@example.com", "testPassword");
        this.loginUser(this.app, robot, "testPatient1@example.com", "testPassword");
        assertEquals(SessionManager.getInstance().getCurrentUser().getId(), user.getId());
        DatabaseManager db = DatabaseManager.getInstance();

        createBooking(robot, 0);

        assertNotNull(db.getAll(Booking.class).stream().findFirst().orElse(null));
    }

    /**
     * Test that double booking displays an error message
     * @param robot
     */
    @Test
    public void testDoubleBooking(FxRobot robot) {
        User user = createPatient("testPatient1@example.com", "testPassword");
        this.loginUser(this.app, robot, "testPatient1@example.com", "testPassword");
        assertEquals(SessionManager.getInstance().getCurrentUser().getId(), user.getId());
        DatabaseManager db = DatabaseManager.getInstance();

        createBooking(robot, 0);
        createBooking(robot, 0);

        String warningText = robot.lookup("#warningMessage").queryAs(WarningMessage.class).getText();
        assertEquals("Cannot make appointment", warningText); // Test for warning text
        assertEquals(1, db.getAll(Booking.class).size()); // Test that there is still only one appointment
    }
}
