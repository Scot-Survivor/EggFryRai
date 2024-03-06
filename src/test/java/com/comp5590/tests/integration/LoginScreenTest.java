package com.comp5590.tests.integration;

import com.comp5590.App;
import com.comp5590.entities.Address;
import com.comp5590.entities.Patient;
import com.comp5590.screens.HomeScreen;
import com.comp5590.screens.LoginScreen;
import com.comp5590.tests.basic.SetupTests;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.testfx.api.FxRobot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(ApplicationExtension.class)  // TestFX Extension)
public class LoginScreenTest extends SetupTests {
    App app;
    Patient testUser;

    @Start  // This is similar to @BeforeAll it will run before all tests,
    // this is where we can get the stage and start the application
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
        app.getScreenManager().showScene(LoginScreen.class); // Force LoginScreen to show.
        stage.show();
    }

    private void addTestUserNoMFA() {
        // Add a test user to the database
        Address address = new Address("1234 Example St", "Test", "AB",
                "12345", "");
        testUser = new Patient();
        testUser.setAddress(address);
        testUser.setEmail("example@example.org");
        testUser.setPassword(app.getPasswordManager().hashPassword("password"));
        app.getDatabase().save(address);
        app.getDatabase().save(testUser);

        // Get the user back from database, this is due to ID being updated.
        SessionFactory sessionFactory = app.getDatabase().getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Patient> patients = session.createQuery("from Patient where email = :email", Patient.class)
                    .setMaxResults(1)
                    .setParameter("email", "example@example.org")
                    .list();
            session.getTransaction().commit();
            testUser = patients.get(0);
        }
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
            // new test ends up in an async race condition so we should sleep for a full second to ensure it doesn't occur
            robot.sleep(1000);  // Sleep for a second to ensure the screen is loaded
            Pane loginScreen = getLoginScreen();
            Set<Node> loginButtons = loginScreen.lookupAll(".button");
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
     * @param robot Will be injected via test runner
     */
    @Test
    public void testSuccessfulLogin(FxRobot robot) {
        this.addTestUserNoMFA();
        robot.interact(() -> {
            Pane loginScreen = getLoginScreen();
            Set<Node> emailFields = loginScreen.lookupAll("#email");
            assertThat(emailFields).isNotNull();
            assertThat(emailFields.size()).isEqualTo(1);
            // Set the email and password fields
            robot.lookup("#email").queryAs(javafx.scene.control.TextField.class)
                    .setText("example@example.org");

            robot.lookup("#password").queryAs(javafx.scene.control.PasswordField.class)
                    .setText("password");

            assertThat(robot.lookup("#email").queryAs(javafx.scene.control.TextField.class).getText())
                    .isEqualTo("example@example.org");
            assertThat(robot.lookup("#password").queryAs(javafx.scene.control.PasswordField.class).getText())
                    .isEqualTo("password");

            robot.lookup("#login").queryAs(javafx.scene.control.Button.class).fire();  // robot.clickOn isn't working rn

            // Check that the home screen is showing
            assertThat(app.getScreenManager().getCurrentScreen()).isInstanceOf(HomeScreen.class);
            assertThat(app.getCurrentUser().getId()).isEqualTo(testUser.getId());
            // Reset back to login screen
            app.getScreenManager().showScene(LoginScreen.class);
        });
    }

    @Test
    public void testFailedLogin(FxRobot robot) {
        this.addTestUserNoMFA();
        robot.interact(() -> {
            Pane loginScreen = getLoginScreen();
            Set<Node> emailFields = loginScreen.lookupAll("#email");
            assertThat(emailFields).isNotNull();
            assertThat(emailFields.size()).isEqualTo(1);
            // Set the email and password fields
            robot.lookup("#email").queryAs(javafx.scene.control.TextField.class)
                    .setText("example@example.org");

            robot.lookup("#password").queryAs(javafx.scene.control.PasswordField.class)
                    .setText("INVALID_PASSWORD");

            assertThat(robot.lookup("#email").queryAs(javafx.scene.control.TextField.class).getText())
                    .isEqualTo("example@example.org");
            assertThat(robot.lookup("#password").queryAs(javafx.scene.control.PasswordField.class).getText())
                    .isEqualTo("INVALID_PASSWORD");

            robot.lookup("#login").queryAs(javafx.scene.control.Button.class).fire();  // robot.clickOn isn't working rn

            // Check that the login screen is still showing
            assertThat(app.getScreenManager().getCurrentScreen()).isInstanceOf(LoginScreen.class);
            assertThat(robot.lookup("#error").queryAs(javafx.scene.control.Label.class).getText())
                    .isNotEmpty();
            // Reset back to login screen
            app.getScreenManager().showScene(LoginScreen.class);
        });
    }
}
