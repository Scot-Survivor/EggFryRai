package com.comp5590.tests.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.comp5590.App;
import com.comp5590.database.entities.User;
import com.comp5590.screens.general.ContactUsScreen;
import com.comp5590.security.managers.mfa.TOTPManager;
import com.comp5590.tests.basic.SetupTests;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.service.query.NodeQuery;

@ExtendWith(ApplicationExtension.class) // TestFX Extension)
public class ContactUsScreenTest extends SetupTests {

    App app;
    TOTPManager totpManager;

    @Start // This is similar to @BeforeAll it will run before all tests,
    // this is where we can get the stage and start the application
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
        this.setAuthenticatedUser(app, new User());
        app.getScreenManager().showScene(ContactUsScreen.class); // Force ContactUs to show.
        totpManager = app.getTotpManager();
        stage.show();
    }

    /**
     * Test that the contact screen has a submit button
     */
    @Test
    public void testScreenHasSubmissionButton(FxRobot robot) {
        robot.interact(() -> {
            // new test ends up in an async race condition so we should sleep for a full
            // second to ensure it doesn't occur
            this.stall(robot);
            // Get submission Button
            NodeQuery submitButton = robot.lookup("#submitButton");
            assertThat(submitButton.queryAll()).isNotNull();
        });
    }

    /**
     * Test that an incomplete submission shows the failure result
     *
     * @param robot The robot to interact with the JavaFX thread
     */
    @Test
    public void testFailingSubmission(FxRobot robot) {
        robot.interact(() -> {
            // Set last name and message fields
            robot.lookup("#lastNameTextBox").queryAs(javafx.scene.control.TextField.class).setText("Doe");
            robot
                .lookup("#messageTextBox")
                .queryAs(javafx.scene.control.TextArea.class)
                .setText("My program is crashing");

            // Check fields actually have data in them
            assertThat(robot.lookup("#lastNameTextBox").queryAs(javafx.scene.control.TextField.class).getText())
                .isEqualTo("Doe");
            assertThat(robot.lookup("#messageTextBox").queryAs(javafx.scene.control.TextArea.class).getText())
                .isEqualTo("My program is crashing");

            robot.lookup("#submitButton").queryAs(javafx.scene.control.Button.class).fire();

            // Check correct label is showing
            assertThat(robot.lookup("#resultLabel").queryAs(javafx.scene.control.Label.class).getText())
                .isEqualTo("You are missing details.");
        });
    }

    @Test
    public void testFailedEmail(FxRobot robot) {
        robot.interact(() -> {
            robot.lookup("#firstNameTextBox").queryAs(javafx.scene.control.TextField.class).setText("John");
            robot.lookup("#lastNameTextBox").queryAs(javafx.scene.control.TextField.class).setText("Doe");
            robot.lookup("#emailTextBox").queryAs(javafx.scene.control.TextField.class).setText("john4");
            robot
                .lookup("#messageTextBox")
                .queryAs(javafx.scene.control.TextArea.class)
                .setText("My program is crashing");

            // Check fields actually have data in them
            assertThat(robot.lookup("#firstNameTextBox").queryAs(javafx.scene.control.TextField.class).getText())
                .isEqualTo("John");
            assertThat(robot.lookup("#lastNameTextBox").queryAs(javafx.scene.control.TextField.class).getText())
                .isEqualTo("Doe");
            assertThat(robot.lookup("#emailTextBox").queryAs(javafx.scene.control.TextField.class).getText())
                .isEqualTo("john4");
            assertThat(robot.lookup("#messageTextBox").queryAs(javafx.scene.control.TextArea.class).getText())
                .isEqualTo("My program is crashing");

            robot.lookup("#submitButton").queryAs(javafx.scene.control.Button.class).fire();

            // Check correct label is showing
            assertThat(robot.lookup("#resultLabel").queryAs(javafx.scene.control.Label.class).getText())
                .isEqualTo("Email is invalid.");
        });
    }

    /**
     * Test that a complete submission shows the success result
     *
     * @param robot The robot to interact with the JavaFX thread
     */
    @Test
    public void testSuccessfulSubmission(FxRobot robot) {
        robot.interact(() -> {
            // Set last name and message fields
            robot.lookup("#firstNameTextBox").queryAs(javafx.scene.control.TextField.class).setText("John");
            robot.lookup("#lastNameTextBox").queryAs(javafx.scene.control.TextField.class).setText("Doe");
            robot.lookup("#emailTextBox").queryAs(javafx.scene.control.TextField.class).setText("johndoe4@gmail.com");
            robot
                .lookup("#messageTextBox")
                .queryAs(javafx.scene.control.TextArea.class)
                .setText("My program is crashing");

            // Check fields actually have data in them
            assertThat(robot.lookup("#firstNameTextBox").queryAs(javafx.scene.control.TextField.class).getText())
                .isEqualTo("John");
            assertThat(robot.lookup("#lastNameTextBox").queryAs(javafx.scene.control.TextField.class).getText())
                .isEqualTo("Doe");
            assertThat(robot.lookup("#emailTextBox").queryAs(javafx.scene.control.TextField.class).getText())
                .isEqualTo("johndoe4@gmail.com");
            assertThat(robot.lookup("#messageTextBox").queryAs(javafx.scene.control.TextArea.class).getText())
                .isEqualTo("My program is crashing");

            robot.lookup("#submitButton").queryAs(javafx.scene.control.Button.class).fire();

            // Check correct label is showing
            assertThat(robot.lookup("#resultLabel").queryAs(javafx.scene.control.Label.class).getText())
                .isEqualTo("Your details have been sent.");

            // Check that fields have been cleared
            assertThat(robot.lookup("#firstNameTextBox").queryAs(javafx.scene.control.TextField.class).getText())
                .isEqualTo("");
            assertThat(robot.lookup("#lastNameTextBox").queryAs(javafx.scene.control.TextField.class).getText())
                .isEqualTo("");
            assertThat(robot.lookup("#emailTextBox").queryAs(javafx.scene.control.TextField.class).getText())
                .isEqualTo("");
            assertThat(robot.lookup("#messageTextBox").queryAs(javafx.scene.control.TextArea.class).getText())
                .isEqualTo("");
        });
    }
}
