package com.comp5590.tests.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.comp5590.App;
import com.comp5590.database.entities.User;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.authentication.LoginScreen;
import com.comp5590.screens.authentication.MFAScreen;
import com.comp5590.screens.authentication.RegisterScreen;
import com.comp5590.screens.general.HomeScreen;
import com.comp5590.security.managers.mfa.TOTPManager;
import com.comp5590.tests.basic.SetupTests;
import com.comp5590.utils.EventUtils;
import com.comp5590.utils.QueryUtils;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.service.query.NodeQuery;

@ExtendWith(ApplicationExtension.class) // TestFX Extension)
public class LoginScreenTest extends SetupTests {

    App app;
    TOTPManager totpManager;

    @Start // This is similar to @BeforeAll it will run before all tests,
    // this is where we can get the stage and start the application
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
        app.getScreenManager().showScene(LoginScreen.class); // Force LoginScreen to show.
        totpManager = app.getTotpManager();
        stage.show();
    }

    // this method will go to the login screen
    private void goToLogin(FxRobot robot) {
        robot.interact(() -> {
            app.getScreenManager().showScene(LoginScreen.class);
        });
    }

    /**
     * Test that the login screen has a login button
     */
    @Test
    public void testScreenHasLoginButton(FxRobot robot) {
        robot.interact(() -> {
            // new test ends up in an async race condition so we should sleep for a full
            // second to ensure it doesn't occur
            this.stall(robot);
            // Get Login Button
            NodeQuery loginButtons = robot.lookup(".big-button");
            assertThat(loginButtons.queryAll()).isNotNull();
        });
    }

    /**
     * Test that the login screen has a password field
     */
    @Test
    public void testScreenHasPasswordField(FxRobot robot) {
        robot.interact(() -> {
            NodeQuery passwordFields = robot.lookup(".password-field");
            assertThat(passwordFields.queryAll()).isNotNull();
        });
    }

    /**
     * Test the login functionality
     *
     * @param robot Will be injected via test runner
     */
    @Test
    public void testSuccessfulLoginNoMFA(FxRobot robot) {
        User p = SetupTests.createPatient("example@example.org", "password");
        robot.interact(() -> {
            // Set the email and password fields
            robot.lookup("#email").queryAs(javafx.scene.control.TextField.class).setText("example@example.org");

            robot.lookup("#password").queryAs(javafx.scene.control.PasswordField.class).setText("password");

            assertThat(robot.lookup("#email").queryAs(javafx.scene.control.TextField.class).getText())
                .isEqualTo("example@example.org");
            assertThat(robot.lookup("#password").queryAs(javafx.scene.control.PasswordField.class).getText())
                .isEqualTo("password");

            robot.lookup("#login").queryAs(javafx.scene.control.Button.class).fire(); // robot.clickOn isn't working rn

            // Check that the home screen is showing
            assertThat(app.getScreenManager().getCurrentScreen()).isInstanceOf(HomeScreen.class);
            assertThat(SessionManager.getInstance().getCurrentUser().getId()).isEqualTo(p.getId());
            // Reset back to login screen
            app.getScreenManager().showScene(LoginScreen.class);
            SessionManager.getInstance().setCurrentUser(null);
        });
        SetupTests.remove(p);
    }

    /**
     * Test login functionality with MFA
     *
     * @param robot Will be injected via test runner
     */
    @Test
    public void testSuccessfulLoginMFARecoveryCode(FxRobot robot) {
        User u = SetupTests.createPatient(
            "mfa@example.org",
            "password",
            totpManager.generateSecret(),
            totpManager.generateRecoveryCodes()
        );
        robot.interact(() -> {
            // Set the email and password fields
            robot.lookup("#email").queryAs(javafx.scene.control.TextField.class).setText("mfa@example.org");

            robot.lookup("#password").queryAs(javafx.scene.control.PasswordField.class).setText("password");

            assertThat(robot.lookup("#email").queryAs(javafx.scene.control.TextField.class).getText())
                .isEqualTo("mfa@example.org");
            assertThat(robot.lookup("#password").queryAs(javafx.scene.control.PasswordField.class).getText())
                .isEqualTo("password");

            robot.lookup("#login").queryAs(javafx.scene.control.Button.class).fire(); // robot.clickOn isn't working rn

            // Check that the MFA screen is showing
            assertThat(app.getScreenManager().getCurrentScreen()).isInstanceOf(MFAScreen.class);

            // Write in MFA codes
            String recoveryCode = u.getAuthenticationDetails().getRecoveryCodes().split(",")[0];
            robot.lookup("#code").queryAs(javafx.scene.control.TextField.class).setText(recoveryCode);

            robot.lookup("#submit").queryAs(javafx.scene.control.Button.class).fire(); // robot.clickOn isn't working rn

            // Check that the home screen is showing
            assertThat(app.getScreenManager().getCurrentScreen()).isInstanceOf(HomeScreen.class);
            assertThat(SessionManager.getInstance().getCurrentUser().getId()).isEqualTo(u.getId());

            // Reset back to login screen
            app.getScreenManager().showScene(LoginScreen.class);
            SessionManager.getInstance().setCurrentUser(null);
        });
        SetupTests.remove(u);
    }

    /**
     * Test login functionality with MFA
     *
     * @param robot Will be injected via test runner
     */
    @Test
    public void testSuccessfulLoginMFAInvalidRecoveryCode(FxRobot robot) {
        User p = SetupTests.createPatient(
            "mfa@example.org",
            "password",
            totpManager.generateSecret(),
            totpManager.generateRecoveryCodes()
        );
        robot.interact(() -> {
            // Set the email and password fields
            robot.lookup("#email").queryAs(javafx.scene.control.TextField.class).setText("mfa@example.org");

            robot.lookup("#password").queryAs(javafx.scene.control.PasswordField.class).setText("password");

            assertThat(robot.lookup("#email").queryAs(javafx.scene.control.TextField.class).getText())
                .isEqualTo("mfa@example.org");
            assertThat(robot.lookup("#password").queryAs(javafx.scene.control.PasswordField.class).getText())
                .isEqualTo("password");

            robot.lookup("#login").queryAs(javafx.scene.control.Button.class).fire(); // robot.clickOn isn't working rn

            // Check that the MFA screen is showing
            assertThat(app.getScreenManager().getCurrentScreen()).isInstanceOf(MFAScreen.class);

            // Write in MFA codes
            String recoveryCode = "THIS WILL BE AN ALWAYS INVALID CODE";
            robot.lookup("#code").queryAs(javafx.scene.control.TextField.class).setText(recoveryCode);

            robot.lookup("#submit").queryAs(javafx.scene.control.Button.class).fire(); // robot.clickOn isn't working rn

            // Check that the home screen is showing
            assertThat(app.getScreenManager().getCurrentScreen()).isInstanceOf(LoginScreen.class);
            assertThat(SessionManager.getInstance().getCurrentUser()).isNull();

            // Reset back to login screen
            app.getScreenManager().showScene(LoginScreen.class);
            SessionManager.getInstance().setCurrentUser(null);
        });
        SetupTests.remove(p);
    }

    @Test
    public void testFailedLogin(FxRobot robot) {
        User p = SetupTests.createPatient("example@example.org", "password");
        robot.interact(() -> {
            // Set the email and password fields
            robot.lookup("#email").queryAs(javafx.scene.control.TextField.class).setText("example@example.org");

            robot.lookup("#password").queryAs(javafx.scene.control.PasswordField.class).setText("INVALID_PASSWORD");

            assertThat(robot.lookup("#email").queryAs(javafx.scene.control.TextField.class).getText())
                .isEqualTo("example@example.org");
            assertThat(robot.lookup("#password").queryAs(javafx.scene.control.PasswordField.class).getText())
                .isEqualTo("INVALID_PASSWORD");

            robot.lookup("#login").queryAs(javafx.scene.control.Button.class).fire(); // robot.clickOn isn't working rn

            // Check that the login screen is still showing
            assertThat(app.getScreenManager().getCurrentScreen()).isInstanceOf(LoginScreen.class);
            assertThat(robot.lookup("#error").queryAs(javafx.scene.control.Label.class).getText()).isNotEmpty();
            // Reset back to login screen
            app.getScreenManager().showScene(LoginScreen.class);
        });
        SetupTests.remove(p);
    }

    @Test
    public void checkScreenAuthenticationListenerPreventsAccessToHomeWithoutAuth(FxRobot robot) {
        robot.interact(() -> {
            SessionManager.getInstance().setCurrentUser(null); // Ensure no user is authenticated
            SessionManager.getInstance().unauthenticate(); // Force de-authentication
            app.getScreenManager().showScene(HomeScreen.class);
            assertThat(app.getScreenManager().getCurrentScreen()).isInstanceOf(LoginScreen.class);
        });
    }

    @Test
    public void testUserSentToRegisterScreenOnBackToRegisterScreenButtonPress(FxRobot robot) {
        goToLogin(robot);
        robot.interact(() -> {
            // Press back to register screen button
            robot
                .lookup("#backToRegisterScreenBox")
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

            // Current screen should be Register Screen after back to register screen button
            // press
            assertInstanceOf(RegisterScreen.class, app.getScreenManager().getCurrentScreen());
        });
    }
}
