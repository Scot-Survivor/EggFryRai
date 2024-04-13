package com.comp5590.tests.integration;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.comp5590.App;
import com.comp5590.screens.authentication.LoginScreen;
import com.comp5590.screens.general.HomeScreen;
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

@ExtendWith(ApplicationExtension.class)
public class HomeScreenTest extends SetupTests {

    App app;

    @Start
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
        stage.show();
    }

    // TODO: add more button tests as we add more buttons to the home screen's
    // navbar
    /*
     * Test that on button click, the user can navigate to each of the screens on
     * the navbar: home, appointments, prescriptions, aboutUs, contactUs, doctors
     */

    @Test
    public void testHomeButton(FxRobot robot) {
        // go to home screen
        goToScreenWithAutoAuthentication(app, robot, HomeScreen.class);

        robot.interact(() -> {
            robot
                .lookup("#home")
                .queryAs(QueryUtils.getButtonClass())
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

            // after waiting for screenbetweenscreens to go away, expect user to be
            // redirected to home screen
            stall(robot);
            assertInstanceOf(HomeScreen.class, app.getScreenManager().getCurrentScreen());
        });
    }

    // Test that the user is logged out on clicking the logout button
    @Test
    public void testLogoutButton(FxRobot robot) {
        // go to home screen
        goToScreenWithAutoAuthentication(app, robot, HomeScreen.class);

        robot.interact(() -> {
            // fire event on logoutBox (click on it)
            robot
                .lookup("#logoutBox")
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
        });

        // after waiting for screenbetweenscreens to go away, expect user to be
        // redirected to login screen
        stall(robot);
        assertInstanceOf(LoginScreen.class, app.getScreenManager().getCurrentScreen());
    }
}
