package com.comp5590.tests.basic;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.comp5590.App;
import com.comp5590.configuration.AppConfig;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.database.utils.EntityUtils;
import com.comp5590.screens.AbstractScreen;
import com.comp5590.screens.HomeScreen;
import com.comp5590.screens.LoginScreen;
import com.comp5590.screens.RegisterScreen;
import com.comp5590.utils.NameUtils;
import com.comp5590.utils.QueryUtils;
import com.comp5590.utils.ScreenUtils;
import com.comp5590.utils.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.testfx.api.FxRobot;

public class SetupTests extends EntityUtils {

    // timeout in MS field
    public static int SCREEN_DELAY_MS = AppConfig.TIMEOUT_MS + 50;

    @BeforeAll
    public static void setup() {
        // Set up the logger
        AppConfig.ConfigFile = "src/test/resources/test.properties";
        AppConfig.getInstance();
    }

    @AfterEach
    public void tearDown() {
        // Here we want to recreate the entire database, this requires some hackery
        DatabaseManager.INSTANCE = null; // By setting to null this should force a reload, due to the way singletons are
        // created
    }

    // TODO: Method for running robot.interact(), then looking up by ID, checking
    // what type it is (and getting appropriate type from QueryUtils), then firing
    // the event

    // Stall method for waiting for the screen to change
    public void stall(FxRobot robot) {
        robot.sleep(SCREEN_DELAY_MS);
    }

    // Register user without login
    public void registerUser(App app, FxRobot robot) {
        // go to register screen
        robot.interact(() -> app.getScreenManager().showScene(RegisterScreen.class));

        // fire generate random user button
        robot.interact(() -> robot.lookup("#generateRandomUserButton").queryAs(QueryUtils.getButtonClass()).fire());

        // hit the register button
        robot.interact(() -> robot.lookup("#registerButton").queryAs(QueryUtils.getButtonClass()).fire());

        // expect login screen to appear
        robot.sleep(SCREEN_DELAY_MS);
        assertInstanceOf(LoginScreen.class, app.getScreenManager().getCurrentScreen());
    }

    // * For those @AuthRequired screens, we need to login first.
    // Register a user
    public void registerUser(App app, FxRobot robot, String email, String password) {
        // go to register screen
        robot.interact(() -> app.getScreenManager().showScene(RegisterScreen.class));

        // fire generate random user button
        robot.lookup("#generateRandomUserButton").queryAs(QueryUtils.getButtonClass()).fire();

        // fill in email and password
        robot.lookup("#email").queryAs(QueryUtils.getTextFieldClass()).setText(email);
        robot.lookup("#password").queryAs(QueryUtils.getPasswordFieldClass()).setText(password);

        // fire register user button to register new user
        robot.interact(() -> robot.lookup("#registerButton").queryAs(QueryUtils.getButtonClass()).fire());

        // wait for screenbetweenscreens to go away
        robot.sleep(SCREEN_DELAY_MS);

        // expect login screen to appear
        assertInstanceOf(LoginScreen.class, app.getScreenManager().getCurrentScreen());
    }

    // Login a user
    public void loginUser(App app, FxRobot robot, String email, String password) {
        // go to login screen
        robot.interact(() -> app.getScreenManager().showScene(LoginScreen.class));

        // fill in email and password
        robot.lookup("#email").queryAs(QueryUtils.getTextFieldClass()).setText(email);
        robot.lookup("#password").queryAs(QueryUtils.getPasswordFieldClass()).setText(password);

        // fire login button
        robot.interact(() -> robot.lookup("#login").queryAs(QueryUtils.getButtonClass()).fire());

        // wait for screenbetweenscreens to go away
        robot.sleep(SCREEN_DELAY_MS);

        // expect home screen to appear
        assertInstanceOf(HomeScreen.class, app.getScreenManager().getCurrentScreen());
    }

    // Go to a specific screen (with appropriate authentication if needed)
    public void goToScreen(App app, FxRobot robot, Class<? extends AbstractScreen> screenClass) {
        // check if this screen has @AuthRequired annotation
        // if so, we need to login & register methods first before doing anything
        if (ScreenUtils.isAuthRequired(screenClass)) {
            // generate an email and password
            String email = NameUtils.getRandomFullEmail();
            String password = StringUtils.randomPassword(8, 64);

            registerUser(app, robot, email, password);
            loginUser(app, robot, email, password);
        }

        // otherwise, just show the screen without authenticating
        robot.interact(() -> app.getScreenManager().showScene(screenClass));
        assertInstanceOf(screenClass, app.getScreenManager().getCurrentScreen());
    }
}
