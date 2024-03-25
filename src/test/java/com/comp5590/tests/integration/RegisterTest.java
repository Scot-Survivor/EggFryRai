package com.comp5590.tests.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.comp5590.App;
import com.comp5590.database.entities.User;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.HomeScreen;
import com.comp5590.screens.LoginScreen;
import com.comp5590.screens.RegisterScreen;
import com.comp5590.tests.basic.SetupTests;
import com.comp5590.utils.EventUtils;
import com.comp5590.utils.NameUtils;
import com.comp5590.utils.QueryUtils;
import com.comp5590.utils.StringUtils;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class RegisterTest extends SetupTests {

    App app;
    private static String TEST_EMAIL = "e9@example.com";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_PHONE_NUMBER = "1234567890";
    private static final String TEST_FAX = "1234567890";
    private static final String TEST_ADDRESS_LINE_ONE = "123 Fake St";
    private static final String TEST_ADDRESS_LINE_TWO = "Canterbury";
    private static final String TEST_ADDRESS_LINE_THREE = "Kent";
    private static final String TEST_COUNTRY = "UK";
    private static final String TEST_POSTCODE = "CT11AA";

    @Start
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
        stage.show();
    }

    /**
     * It has to run on the JavaFX Thread to avoid race conditions, hence the robot
     * parameter
     *
     * @param robot The robot to interact with the JavaFX thread
     */
    private void goToRegister(FxRobot robot) {
        robot.interact(() -> {
            app.getScreenManager().showScene(RegisterScreen.class);
        });
        assertInstanceOf(RegisterScreen.class, app.getScreenManager().getCurrentScreen());
    }

    /**
     * It has to run on the JavaFX Thread to avoid race conditions, hence the robot
     * parameter
     *
     * @param robot The robot to interact with the JavaFX thread
     */
    private void inputInformation(FxRobot robot) {
        robot.interact(() -> {
            robot.lookup("#email").queryAs(TextField.class).setText(TEST_EMAIL);
            robot.lookup("#password").queryAs(TextField.class).setText(TEST_PASSWORD);
            robot.lookup("#firstName").queryAs(TextField.class).setText(TEST_FIRST_NAME);
            robot.lookup("#surName").queryAs(TextField.class).setText(TEST_LAST_NAME);
            robot.lookup("#phone").queryAs(TextField.class).setText(TEST_PHONE_NUMBER);
            robot.lookup("#fax").queryAs(TextField.class).setText(TEST_FAX);
            robot.lookup("#addressLine1").queryAs(TextField.class).setText(TEST_ADDRESS_LINE_ONE);
            robot.lookup("#addressLine2").queryAs(TextField.class).setText(TEST_ADDRESS_LINE_TWO);
            robot.lookup("#addressLine3").queryAs(TextField.class).setText(TEST_ADDRESS_LINE_THREE);
            robot.lookup("#country").queryAs(TextField.class).setText(TEST_COUNTRY);
            robot.lookup("#postcode").queryAs(TextField.class).setText(TEST_POSTCODE);
            // ComboBox "role", set to "Patient"
            robot.lookup("#role").queryAs(ComboBox.class).getSelectionModel().select(0);
            robot.lookup("#communicationPreference").queryAs(ComboBox.class).getSelectionModel().select(0);
        });
    }

    /**
     * It has to run on the JavaFX Thread to avoid race conditions, hence the robot
     * parameter
     *
     * @param robot The robot to interact with the JavaFX thread
     */
    private void cleanUpUser(FxRobot robot) {
        robot.interact(() -> {
            int userId = getDbManager().getByProperty(User.class, "authenticationDetails.email", TEST_EMAIL).getId();
            SetupTests.remove(User.class, userId);
            SessionManager.getInstance().setCurrentUser(null);
            SessionManager.getInstance().unauthenticate();
            assertNull(getDbManager().get(User.class, userId));
        });
    }

    @Test
    public void testThatAUserCanRegister(FxRobot robot) {
        // go to register screen
        goToScreen(app, robot, RegisterScreen.class);

        // click random user generate button
        robot.interact(() -> robot.lookup("#generateRandomUserButton").queryButton().fire());

        // click register button
        robot.interact(() -> robot.lookup("#registerButton").queryButton().fire());

        // stall, expect user to be redirected to LoginScreen
        stall(robot);
        assertInstanceOf(LoginScreen.class, app.getScreenManager().getCurrentScreen());
    }

    @Test
    public void testUserSentToLoginScreenOnBackToLoginScreenButtonPress(FxRobot robot) {
        goToRegister(robot);
        robot.interact(() -> {
            // Press back to login screen button
            robot
                .lookup("#backToLoginScreenBox")
                .queryAs(QueryUtils.getHBoxClass())
                .fireEvent(
                    EventUtils.createCustomMouseEvent(
                        MouseEvent.MOUSE_CLICKED,
                        0,
                        0,
                        0,
                        0,
                        MouseButton.PRIMARY,
                        1,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        null
                    )
                );

            // Current screen should be Login Screen after back to login screen button press
            assertInstanceOf(LoginScreen.class, app.getScreenManager().getCurrentScreen());
        });
    }

    @Test
    public void testThatAUserCanLoginAfterRegister(FxRobot robot) {
        // define basic user details
        String email = NameUtils.getRandomFullEmail();
        String password = StringUtils.randomPassword(8, 64);

        // register a user
        registerUser(app, robot, email, password);

        // go to login screen
        stall(robot);
        assertInstanceOf(LoginScreen.class, app.getScreenManager().getCurrentScreen());

        // try logging in
        loginUser(app, robot, email, password);

        // Current screen should be Home Screen after login
        assertInstanceOf(HomeScreen.class, app.getScreenManager().getCurrentScreen());
    }

    @Test
    public void testThatAUserCannotRegisterWithInvalidEmail(FxRobot robot) {
        goToRegister(robot);
        String oldEmail = TEST_EMAIL;
        TEST_EMAIL = "example";
        inputInformation(robot);
        robot.interact(() -> {
            robot.lookup("#registerButton").queryButton().fire(); // Actually register
            assertInstanceOf(RegisterScreen.class, app.getScreenManager().getCurrentScreen()); // Current screen should
            // be Register Screen
            // after invalid email
        });
        TEST_EMAIL = oldEmail;
    }

    @Test
    public void testGenerateRandomUserButtonFieldsNotEmpty(FxRobot robot) {
        goToRegister(robot);
        robot.interact(() -> {
            robot.lookup("#generateRandomUserButton").queryButton().fire();
            assertFalse(robot.lookup("#email").queryAs(TextField.class).getText().isEmpty());
            assertFalse(robot.lookup("#password").queryAs(TextField.class).getText().isEmpty());
            assertFalse(robot.lookup("#firstName").queryAs(TextField.class).getText().isEmpty());
            assertFalse(robot.lookup("#surName").queryAs(TextField.class).getText().isEmpty());
            assertFalse(robot.lookup("#phone").queryAs(TextField.class).getText().isEmpty());
            assertFalse(robot.lookup("#fax").queryAs(TextField.class).getText().isEmpty());
            assertFalse(robot.lookup("#addressLine1").queryAs(TextField.class).getText().isEmpty());
            assertFalse(robot.lookup("#addressLine2").queryAs(TextField.class).getText().isEmpty());
            assertFalse(robot.lookup("#addressLine3").queryAs(TextField.class).getText().isEmpty());
            assertFalse(robot.lookup("#country").queryAs(TextField.class).getText().isEmpty());
            assertFalse(robot.lookup("#postcode").queryAs(TextField.class).getText().isEmpty());
        });
    }

    @Test
    public void testRegistrationSucceedsWithRandomUserButtonDetails(FxRobot robot) {
        registerUser(app, robot);
        assertInstanceOf(LoginScreen.class, app.getScreenManager().getCurrentScreen());
    }

    @Test
    public void testErrorLabelClearsOnReturnFromLoginScreen(FxRobot robot) {
        goToRegister(robot);
        robot.interact(() -> {
            // create an error message on register screen programmatically
            robot.lookup("#error").queryAs(javafx.scene.control.Label.class).setText("Error");
            // go to login screen from register screen, using scene manager
            app.getScreenManager().showScene(LoginScreen.class);
            // go back to register screen from login screen, using scene manager
            app.getScreenManager().showScene(RegisterScreen.class);
            // check that the error message is cleared
            assertEquals("", robot.lookup("#error").queryAs(javafx.scene.control.Label.class).getText());
        });
    }
}
