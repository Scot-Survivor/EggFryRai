package com.comp5590.tests.basic;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.comp5590.App;
import com.comp5590.configuration.AppConfig;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.database.utils.EntityUtils;
import com.comp5590.screens.authentication.LoginScreen;
import com.comp5590.screens.authentication.RegisterScreen;
import com.comp5590.screens.general.HomeScreen;
import com.comp5590.screens.misc.AbstractScreen;
import com.comp5590.utils.NameUtils;
import com.comp5590.utils.QueryUtils;
import com.comp5590.utils.ScreenUtils;
import com.comp5590.utils.StringUtils;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
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
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
    }

    @AfterEach
    public void tearDown() {
        // Here we want to recreate the entire database, this requires some hackery
        DatabaseManager.INSTANCE = null; // By setting to null this should force a reload, due to the way singletons are
        // created
        System.gc();
    }

    // TODO: Method for running robot.interact(), then looking up by ID, checking
    // what type it is (and getting appropriate type from QueryUtils), then firing
    // the event

    /**
     * Stall method for waiting for the screen to change
     */

    public void stall(FxRobot robot) {
        this.stall(robot, SCREEN_DELAY_MS);
    }

    /**
     * Stall method for waiting for the screen to change
     * @param robot FxRobot instance
     * @param delay Delay in milliseconds
     */
    public void stall(FxRobot robot, int delay) {
        robot.sleep(delay);
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
        stall(robot);
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
        stall(robot);

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
        stall(robot);

        // expect home screen to appear
        assertInstanceOf(HomeScreen.class, app.getScreenManager().getCurrentScreen());
    }

    // Set SessionManager.currentUser to a random user
    public void authenticateAsRandomUser(App app, FxRobot robot) {
        // generate an email and password
        String email = NameUtils.getRandomFullEmail();
        String password = StringUtils.randomPassword(8, 64);

        // register and login user
        registerUser(app, robot, email, password);
        loginUser(app, robot, email, password);
    }

    // Go to a specific screen (with appropriate authentication if needed)
    public void goToScreenWithAutoAuthentication(App app, FxRobot robot, Class<? extends AbstractScreen> screenClass) {
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

    public void logout(App app, FxRobot robot) {
        robot.interact(() -> {
            app.getSessionManager().unauthenticate();
            app.getScreenManager().showScene(LoginScreen.class);
        });
    }

    /**
     * Go to a specific screen (with appropriate authentication if needed)
     * @param app App instance
     * @param robot FxRobot instance
     * @param screenClass Screen class to go to
     * @param email Email to use for authentication
     * @param password Password to use for authentication
     */
    public void goToScreenWithAuthentication(
        App app,
        FxRobot robot,
        Class<? extends AbstractScreen> screenClass,
        String email,
        String password
    ) {
        // check if this screen has @AuthRequired annotation
        // if so, we need to login & register methods first before doing anything
        if (ScreenUtils.isAuthRequired(screenClass)) {
            // registerUser(app, robot, email, password);
            loginUser(app, robot, email, password);
        }

        // otherwise, just show the screen without authenticating
        robot.interact(() -> app.getScreenManager().showScene(screenClass));
        assertInstanceOf(screenClass, app.getScreenManager().getCurrentScreen());
    }

    // Go to screen without any authentication (needed if using a custom user in the
    // TestFile.setupDB(), e.g., for testing if the correct data is displayed on the
    // screen associated with that user).
    public void goToScreen(App app, FxRobot robot, Class<? extends AbstractScreen> screenClass) {
        robot.interact(() -> app.getScreenManager().showScene(screenClass));
        assertInstanceOf(screenClass, app.getScreenManager().getCurrentScreen());
    }

    // Set the current user to a specific user
    public void setAuthenticatedUser(App app, User user) {
        app.getSessionManager().authenticate(user);
    }

    // QOL methods (consider BST, NO ZONE ID)
    public Date createFutureDate(int days) {
        // get current date
        LocalDate now = LocalDate.now();

        // add days
        LocalDate future = now.plus(days, ChronoUnit.DAYS);

        // convert to date, and return it
        return java.sql.Date.valueOf(future);
    }

    public void resetDatabase() {
        DatabaseManager.INSTANCE = null;
    }
}
