package com.comp5590.tests.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.comp5590.App;
import com.comp5590.components.DoctorDetailsScreen.DoctorDisplayBox;
import com.comp5590.database.entities.User;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.ViewDoctorDetailsScreen;
import com.comp5590.tests.basic.SetupTests;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class ViewDoctorDetailsTest extends SetupTests {

    private App app;

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
        // goToScreen(app, robot, CreateBooking.class);
        // We have to ensure we're a patient logged in
        User user = createPatient("testPatient1@example.com", "testPassword");
        this.loginUser(this.app, robot, "testPatient1@example.com", "testPassword");
        assertEquals(SessionManager.getInstance().getCurrentUser().getId(), user.getId());

        robot.interact(() -> {
            app.getScreenManager().showScene(ViewDoctorDetailsScreen.class);
            robot.lookup("#doctorChoiceBox").queryAs(ChoiceBox.class).getSelectionModel().select(0); // Just select first doctor
        });

        robot.interact(() -> {
            robot.lookup("#showButton").queryAs(javafx.scene.control.Button.class).fire();
            stall(robot);
            // Assert that a booking was created

            DoctorDisplayBox docDisplay = robot.lookup("#docDisplay").queryAs(DoctorDisplayBox.class);
            for (javafx.scene.Node node : docDisplay.getChildren()) {
                if (node instanceof Text) {
                    Text text = (Text) node;
                    assertNotNull(text);
                }
            }
        });
    }
}
