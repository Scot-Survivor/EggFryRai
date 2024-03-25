package com.comp5590.tests.basic;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.comp5590.App;
import com.comp5590.configuration.AppConfig;
import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.AuthenticationDetails;
import com.comp5590.database.entities.Room;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.enums.CommunicationPreference;
import com.comp5590.enums.UserRole;
import com.comp5590.screens.AbstractScreen;
import com.comp5590.screens.HomeScreen;
import com.comp5590.screens.LoginScreen;
import com.comp5590.screens.RegisterScreen;
import com.comp5590.security.managers.passwords.PasswordManager;
import com.comp5590.utils.NameUtils;
import com.comp5590.utils.QueryUtils;
import com.comp5590.utils.ScreenUtils;
import com.comp5590.utils.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.testfx.api.FxRobot;

public class SetupTests {

    // timeout in MS field
    public static int SCREEN_DELAY_MS = AppConfig.TIMEOUT_MS + 50;

    @BeforeAll
    public static void setup() {
        // Set up the logger
        AppConfig.ConfigFile = "src/test/resources/test.properties";
        AppConfig.getInstance();
    }

    // QOL Of Methods
    public static DatabaseManager getDbManager() {
        return DatabaseManager.getInstance();
    }

    /**
     * Create an address object with default values.
     *
     * @return Address
     */
    public static Address createAddress() {
        Address address = new Address();
        address.setPostCode("CT1 1AA");
        address.setCountry("GB");
        address.setAddressLineOne("1 Test Street");
        address.setAddressLineTwo("Test Town");
        address.setAddressLineThree("Test County");
        address = getDbManager().saveGet(address);
        return address;
    }

    /**
     * Create authentication details without mfa
     *
     * @return AuthenticationDetails
     */
    public static AuthenticationDetails createAuthenticationDetails(String email, String password) {
        AuthenticationDetails authenticationDetails = new AuthenticationDetails();
        authenticationDetails.setEmail(email);
        authenticationDetails.setPassword(PasswordManager.getInstanceOf("Argon2").hashPassword(password));
        authenticationDetails.setTwoFactorEnabled(false);
        authenticationDetails.setAuthenticationToken("");
        authenticationDetails.setRecoveryCodes("");
        authenticationDetails = getDbManager().saveGet(authenticationDetails);
        return authenticationDetails;
    }

    /**
     * Create authentication details with mfa
     *
     * @return AuthenticationDetails
     */
    public static AuthenticationDetails createAuthenticationDetails(
        String email,
        String password,
        String authenticationToken,
        String recoveryCodes
    ) {
        AuthenticationDetails authenticationDetails = createAuthenticationDetails(email, password);
        authenticationDetails.setTwoFactorEnabled(true);
        authenticationDetails.setAuthenticationToken(authenticationToken);
        authenticationDetails.setRecoveryCodes(recoveryCodes);
        // update
        getDbManager().update(authenticationDetails);
        authenticationDetails = getDbManager().get(AuthenticationDetails.class, authenticationDetails.getId());
        return authenticationDetails;
    }

    /**
     * Create a patient user with all filled values,
     *
     * @return User
     */
    public static User createPatient(AuthenticationDetails authenticationDetails) {
        Address address = createAddress();
        User user = new User(
            "Test",
            "User",
            "0123456789",
            "0123456789",
            "Test Notes",
            CommunicationPreference.EMAIL,
            UserRole.PATIENT,
            address
        );
        user.setRole(UserRole.PATIENT);
        user.setAuthenticationDetails(authenticationDetails);
        user = getDbManager().saveGet(user);
        return user;
    }

    /**
     * Create a user object with the email and password specified.
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @return User
     */
    public static User createPatient(String email, String password) {
        return createPatient(createAuthenticationDetails(email, password));
    }

    /**
     * Create a user object with the email and password specified.
     *
     * @param email               the email of the user
     * @param password            the password of the user
     * @param authenticationToken the authentication token
     * @param recoveryCodes       the recovery codes
     * @return User
     */
    public static User createPatient(String email, String password, String authenticationToken, String recoveryCodes) {
        return createPatient(createAuthenticationDetails(email, password, authenticationToken, recoveryCodes));
    }

    /**
     * Create a doctor user with all filled values,
     *
     * @return User
     */
    public static User createDoctor(AuthenticationDetails authenticationDetails) {
        Address address = createAddress();
        User user = new User(
            "Test",
            "User",
            "0123456789",
            "0123456789",
            "Test Notes",
            CommunicationPreference.EMAIL,
            UserRole.DOCTOR,
            address
        );
        user.setRole(UserRole.DOCTOR);
        user.setAuthenticationDetails(authenticationDetails);
        int id = getDbManager().saveGetId(user);
        user = getDbManager().get(User.class, id);
        return user;
    }

    /**
     * Create a user object with the email and password specified.
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @return User
     */
    public static User createDoctor(String email, String password) {
        return createDoctor(createAuthenticationDetails(email, password));
    }

    /**
     * Create a Room object using roomNum and address
     *
     * @param roomNum The number of the room
     * @param address The address of the room
     * @return The room
     */
    public Room makeRoom(String roomNum, Address address) {
        Room room = new Room();
        room.setAddress(address);
        room.setRoomNumber(roomNum);
        room = getDbManager().saveGet(room);
        return room;
    }

    /**
     * Remove an object from the database by id
     */
    public static <T> void remove(final Class<T> type, int id) {
        getDbManager().delete(getDbManager().get(type, id));
    }

    /**
     * Complete remove a user object
     */
    public static void remove(User user) {
        remove(AuthenticationDetails.class, user.getAuthenticationDetails().getId());
        remove(User.class, user.getId());
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
