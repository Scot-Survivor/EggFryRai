package com.comp5590.tests.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.comp5590.App;
import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.Booking;
import com.comp5590.database.managers.DatabaseManager;
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
     * Test filling in the form and booking an appointment
     * @param robot
     */
    @Test
    public void testBookingCreation(FxRobot robot) {
        goToScreen(app, robot, CreateBooking.class);
        DatabaseManager db = DatabaseManager.getInstance();
        robot.interact(() -> {
            robot.lookup("#doctorChoiceBox").queryAs(ChoiceBox.class).getSelectionModel().select(0); // Just select first doctor
            robot.lookup("#roomChoiceBox").queryAs(ChoiceBox.class).getSelectionModel().select(0); // just select first room
            robot.lookup("#time").queryAs(ChoiceBox.class).getSelectionModel().select(0); // just select first time
            robot.lookup("#apptReasonTextField").queryAs(TextField.class).setText(APPOINTMENT_REASON);
            robot.lookup("#datePicker").queryAs(DatePicker.class).setValue(APPOINTMENT_TIME);
        });

        robot.interact(() -> {
            robot.lookup("#bookingButton").queryAs(javafx.scene.control.Button.class).fire();
            stall(robot);
            // Assert that a booking was created
            assertNotNull(db.getAll(Booking.class).stream().findFirst().orElse(null));
        });
    }
}
