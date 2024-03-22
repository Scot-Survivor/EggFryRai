package com.comp5590.tests.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.comp5590.App;
import com.comp5590.database.entities.User;
import com.comp5590.screens.HomeScreen;
import com.comp5590.screens.LoginScreen;
import com.comp5590.screens.MFAScreen;
import com.comp5590.security.managers.mfa.TOTPManager;
import com.comp5590.tests.basic.SetupTests;
import java.util.Set;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

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

    private Pane getLoginScreen() {
        return (Pane) app.getScreenManager().getScreens().get(LoginScreen.class).getRoot();
    }

    /**
     * Test that the login screen has a login button
     */
    @Test
    public void testScreenHasLoginButton(FxRobot robot) {
        robot.interact(() -> {
            // new test ends up in an async race condition so we should sleep for a full
            // second to ensure it doesn't occur
            robot.sleep(1000); // Sleep for a second to ensure the screen is loaded
            Pane loginScreen = getLoginScreen();
            Set<Node> loginButtons = loginScreen.lookupAll(".big-button");
            assertThat(loginButtons).isNotNull();
            assertThat(loginButtons.size()).isEqualTo(1);
        });
    }

    /**
     * Test that the login screen has a password field
     */
    @Test
    public void testScreenHasPasswordField(FxRobot robot) {
        robot.interact(() -> {
            Pane loginScreen = getLoginScreen();
            Set<Node> passwordFields = loginScreen.lookupAll(".password-field");
            assertThat(passwordFields).isNotNull();
            assertThat(passwordFields.size()).isEqualTo(1);
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
            Pane loginScreen = getLoginScreen();
            Set<Node> emailFields = loginScreen.lookupAll("#email");
            assertThat(emailFields).isNotNull();
            assertThat(emailFields.size()).isEqualTo(1);
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
            assertThat(app.getCurrentUser().getId()).isEqualTo(p.getId());
            // Reset back to login screen
            app.getScreenManager().showScene(LoginScreen.class);
            app.setCurrentUser(null);
        });
        SetupTests.remove(p.getClass(), p.getId());
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
            Pane loginScreen = getLoginScreen();
            Set<Node> emailFields = loginScreen.lookupAll("#email");
            assertThat(emailFields).isNotNull();
            assertThat(emailFields.size()).isEqualTo(1);
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
            assertThat(app.getCurrentUser().getId()).isEqualTo(u.getId());

            // Reset back to login screen
            app.getScreenManager().showScene(LoginScreen.class);
            app.setCurrentUser(null);
        });
        SetupTests.remove(u.getClass(), u.getId());
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
            Pane loginScreen = getLoginScreen();
            Set<Node> emailFields = loginScreen.lookupAll("#email");
            assertThat(emailFields).isNotNull();
            assertThat(emailFields.size()).isEqualTo(1);
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
            assertThat(app.getCurrentUser()).isNull();

            // Reset back to login screen
            app.getScreenManager().showScene(LoginScreen.class);
            app.setCurrentUser(null);
        });
        SetupTests.remove(p.getClass(), p.getId());
    }

    @Test
    public void testFailedLogin(FxRobot robot) {
        User p = SetupTests.createPatient("example@example.org", "password");
        robot.interact(() -> {
            Pane loginScreen = getLoginScreen();
            Set<Node> emailFields = loginScreen.lookupAll("#email");
            assertThat(emailFields).isNotNull();
            assertThat(emailFields.size()).isEqualTo(1);
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
        SetupTests.remove(p.getClass(), p.getId());
    }

    @Test
    public void checkScreenAuthenticationListenerPreventsAccessToHomeWithoutAuth(FxRobot robot) {
        robot.interact(() -> {
            app.setCurrentUser(null); // Ensure no user is authenticated
            app.getSessionManager().setAuthenticated(false); // Force de-authentication
            app.getScreenManager().showScene(HomeScreen.class);
            assertThat(app.getScreenManager().getCurrentScreen()).isInstanceOf(LoginScreen.class);
        });
    }
}
