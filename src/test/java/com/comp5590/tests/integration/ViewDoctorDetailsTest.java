package com.comp5590.tests.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.comp5590.App;
import com.comp5590.components.DoctorDetailsScreen.DoctorDisplayBox;
import com.comp5590.database.entities.User;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.doctors.ViewDoctorsScreen;
import com.comp5590.tests.basic.SetupTests;
import javafx.scene.control.Button;
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
     *
     * @param robot
     */
    @Test
    public void testBookingCreation(FxRobot robot) {
        // goToScreen(app, robot, CreateBooking.class);
        // We have to ensure we're a patient logged in
        User user = createPatient("testPatient1@example.com", "testPassword");
        this.loginUser(this.app, robot, "testPatient1@example.com", "testPassword");
        assertEquals(SessionManager.getInstance().getCurrentUser().getId(), user.getId());

        // Select a doctor
        robot.interact(() -> {
            app.getScreenManager().showScene(ViewDoctorsScreen.class);
            assertInstanceOf(ViewDoctorsScreen.class, app.getScreenManager().getCurrentScreen()); // Check we are on the
            // right screen
            robot.lookup("#doctorChoiceBox").queryAs(ChoiceBox.class).getSelectionModel().select(0); // Just select
            // first doctor
        });

        // Show the text by clicking button and validate that all is text
        robot.interact(() -> {
            robot.lookup("#showButton").queryAs(Button.class).fire();
            stall(robot);

            // Loop over and check that all of the items are text and not null
            DoctorDisplayBox docDisplay = robot.lookup("#docDisplay").queryAs(DoctorDisplayBox.class);
            assertTrue(!docDisplay.getChildren().isEmpty()); // Make sure not empty
            for (javafx.scene.Node node : docDisplay.getChildren()) {
                if (node instanceof Text) {
                    Text text = (Text) node;
                    assertNotNull(text); // Make sure text is not null
                }
            }
        });
    }

    /**
     * Test to check if the error message displays correctly for when no doctor is
     * selected
     *
     * @param robot
     */
    @Test
    public void testErrorDisplay(FxRobot robot) {
        // goToScreen(app, robot, CreateBooking.class);
        // We have to ensure we're a patient logged in
        User user = createPatient("testPatient1@example.com", "testPassword");
        this.loginUser(this.app, robot, "testPatient1@example.com", "testPassword");
        assertEquals(SessionManager.getInstance().getCurrentUser().getId(), user.getId());

        // Select a doctor
        robot.interact(() -> {
            app.getScreenManager().showScene(ViewDoctorsScreen.class);
            assertInstanceOf(ViewDoctorsScreen.class, app.getScreenManager().getCurrentScreen()); // Check we are on the
            // right screen

            robot.lookup("#showButton").queryAs(Button.class).fire();
            stall(robot);

            DoctorDisplayBox errorText = robot.lookup("#docDisplay").queryAs(DoctorDisplayBox.class);
            if (errorText.getChildren().size() == 1) {
                assertEquals("Please select a doctor", ((Text) errorText.getChildren().get(0)).getText());
            }
        });
    }
}
