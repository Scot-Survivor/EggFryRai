package com.comp5590.screens.misc;

import com.comp5590.App;
import com.comp5590.components.HomeScreen.HeaderBar;
import com.comp5590.components.HomeScreen.NavBar;
import com.comp5590.configuration.AppConfig;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.authentication.LoginScreen;
import com.comp5590.screens.bookings.CreateBookingScreen;
import com.comp5590.screens.bookings.ViewAndChangeBookingsScreen;
import com.comp5590.screens.bookings.VisitDetailsScreen;
import com.comp5590.screens.doctors.ChooseDoctorScreen;
import com.comp5590.screens.doctors.ViewDoctorsScreen;
import com.comp5590.screens.general.ContactUsScreen;
import com.comp5590.screens.general.HomeScreen;
import com.comp5590.screens.general.WelcomeScreen;
import com.comp5590.screens.general.settings.ChangeUserInfoScreen;
import com.comp5590.screens.general.settings.MFASettingsScreen;
import com.comp5590.screens.general.settings.ProfileScreen;
import com.comp5590.screens.loading.ScreenBetweenScreens;
import com.comp5590.screens.managers.ScreenManager;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.core.Logger;
import org.hibernate.SessionFactory;

/**
 * AbstractScreen class
 * <p>
 * This class is the base class for all screens in the application.
 * It provides the basic functionality for all screens, such as
 * setting up the screen, cleaning up the screen, and checking if
 * the user has access to the screen.
 * It also provides methods for adding and removing CSS files to the screen.
 * It also provides methods for adding back and home buttons to the screen.
 * It also provides methods for showing the screen and transitioning between
 * screens.
 * It also provides the app, database manager, screen manager, session factory,
 * and logger objects.
 * It also provides the root pane and CSS paths for the screen.
 * </p>
 */
@Getter
public abstract class AbstractScreen {

    private final App app;
    private final DatabaseManager databaseManager;
    private final ScreenManager screenManager;
    private final SessionFactory sessionFactory;
    private final Logger logger = LoggerManager.getInstance().getLogger(AbstractScreen.class, "DEBUG");

    @Setter
    private Pane rootPane;

    /**
     * The paths to the CSS file for the screen
     */
    @Getter
    private final List<String> cssPaths;

    public AbstractScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.app = App.getInstance();
        this.databaseManager = DatabaseManager.getInstance();
        this.sessionFactory = this.databaseManager.getSessionFactory();
        this.cssPaths = new ArrayList<>();
    }

    /**
     * This method is called when the screen is created
     */
    public abstract void setup();

    /**
     * This method is called when the scene is changed, to clean it up
     * Every screen must implement this method
     * Examples of cleanup include removing event listeners, clearing text fields,
     * etc.
     */
    public abstract void cleanup();

    /**
     * This method is called when the screen is shown, to ensure that the logged in
     * user has access to the screen
     *
     * @return true if the user has access to the screen
     */
    public boolean canAccess() {
        return true;
    }

    /**
     * This method is called when the screen is shown, to ensure that the logged in
     * user has access to the screen
     *
     * @param user object
     * @return true if the user has access to the screen
     */
    public boolean canAccess(User user) {
        return true;
    }

    /**
     * Add new CSS file to list
     */
    public void addCss(String cssPath) {
        // Check CSS Path exists first
        if (cssPath == null || cssPath.isEmpty()) {
            logger.warn("CSS Path is empty or null");
            return;
        }
        if (getClass().getResource(cssPath) == null) {
            logger.warn("CSS Path does not exist in resource: " + cssPath);
            return;
        }
        cssPaths.add(cssPath);
    }

    /**
     * Remove CSS File from list
     */
    public void removeCss(String cssPath) {
        cssPaths.remove(cssPath);
    }

    /**
     * Attach default pane to screen
     *
     * @return GridPane
     */
    public GridPane attachDefaultPane() {
        this.addCss("/css/global.css");

        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("custom-pane");

        // create the border pane (which will serve as root pane)
        // set grid pane as child of border pane
        BorderPane rootPane = new BorderPane();
        rootPane.setPadding(new Insets(0, 0, 0, 0));
        rootPane.setCenter(gridPane);

        setRootPane(rootPane); // set root pane

        return gridPane;
    }

    protected HeaderBar createHeaderBar(int type, String leftText, String leftIconURL, String title) {
        // create leftBox for the left side of the header bar
        HBox leftBox = new HBox();
        if (type == 1) {
            leftBox.setId("profileBox");

            // attach event listener to leftBox
            leftBox.setOnMouseClicked(e -> {
                logger.info("Profile button clicked");
                showScene(ProfileScreen.class);
            });
        } else if (type == 2) {
            leftBox.setId("backToHomeBox");

            // attach event listener to leftBox
            leftBox.setOnMouseClicked(e -> {
                logger.info("Home button clicked");
                showScene(HomeScreen.class);
            });
        }

        // create hbox for the logout event listener
        HBox logoutBox = new HBox();
        logoutBox.setId("logoutBox");

        // attach event listener to logoutbox
        logoutBox.setOnMouseClicked(e -> {
            logger.info("Logout button clicked");
            SessionManager.getInstance().unauthenticate();
            this.showSceneBetweenScenesThenNextScene(
                    "👋 You have successfully logged out.\nRedirecting to login screen...",
                    LoginScreen.class
                );
        });

        // grab current user
        User curUser = SessionManager.getInstance().getCurrentUser();
        String firstname = "Test";
        if (curUser != null) {
            firstname = curUser.getFirstName();
        }

        // Create the header bar with the determined name
        return new HeaderBar(leftBox, leftText, leftIconURL, logoutBox, firstname, title);
    }

    protected NavBar createNavBar(int type) {
        NavBar navbar = null;

        if (type == 1) {
            // create Buttons for each navbar item
            Button home = new Button("Home");
            Button visitDetails = new Button("View Visit Details");
            Button newAppointment = new Button("New Appointment");
            Button viewAndChangeAppointments = new Button("View / Change Appointment");
            Button aboutUs = new Button("About us");
            Button contactUs = new Button("Contact us");
            Button chooseDoctor = new Button("Choose Doctor");
            Button viewDoctors = new Button("View Doctors");
            Button logs = new Button("Logs");

            // add IDs to buttons for testing purposes
            home.setId("home");
            visitDetails.setId("visitDetails");
            newAppointment.setId("newAppointment");
            viewAndChangeAppointments.setId("viewAndChangeAppointments");
            aboutUs.setId("aboutUs");
            contactUs.setId("contactUs");
            chooseDoctor.setId("chooseDoctor");
            viewDoctors.setId("viewDoctors");
            logs.setId("logs");

            // attach event listeners to each button
            home.setOnAction(e -> {
                logger.info("Home button clicked");
                showScene(HomeScreen.class);
            });

            visitDetails.setOnAction(e -> {
                logger.info("Visit Details button clicked");
                showScene(VisitDetailsScreen.class);
            });

            newAppointment.setOnAction(e -> {
                logger.info("New Appointment button clicked");
                showScene(CreateBookingScreen.class);
            });

            viewAndChangeAppointments.setOnAction(e -> {
                logger.info("View and Change Appointments button clicked");
                showScene(ViewAndChangeBookingsScreen.class);
            });

            aboutUs.setOnAction(e -> {
                logger.info("About us button clicked");
                showScene(AboutUsScreen.class);
            });

            contactUs.setOnAction(e -> {
                logger.info("Contact us button clicked");
                showScene(ContactUsScreen.class);
            });

            chooseDoctor.setOnAction(e -> {
                logger.info("ChooseDoctor button clicked");
                showScene(ChooseDoctorScreen.class);
            });

            viewDoctors.setOnAction(e -> {
                logger.info("ViewDoctors button clicked");
                showScene(ViewDoctorsScreen.class);
            });

            logs.setOnAction(e -> {
                logger.info("Logs button clicked");
                showScene(LoggingScreen.class);
            });

            // create the navbar
            navbar =
            new NavBar(
                home,
                visitDetails,
                newAppointment,
                viewAndChangeAppointments,
                aboutUs,
                contactUs,
                chooseDoctor,
                viewDoctors,
                logs
            );
        } else if (type == 2) {
            // create Buttons for each navbar item
            Button profile = new Button("Profile");
            Button mfaSettings = new Button("MFA Settings");
            Button editProfile = new Button("Edit your profile");

            // add IDs to buttons for testing purposes
            profile.setId("profile");
            mfaSettings.setId("mfaSettings");
            editProfile.setId("editProfile");

            // attach event listeners to each button
            profile.setOnAction(e -> {
                logger.info("Profile button clicked");
                showScene(ProfileScreen.class);
            });

            mfaSettings.setOnAction(e -> {
                logger.info("MFA Settings button clicked");
                showScene(MFASettingsScreen.class);
            });

            editProfile.setOnAction(e -> {
                logger.info("Edit Profile button clicked");
                showScene(ChangeUserInfoScreen.class);
            });

            // create the navbar
            navbar = new NavBar(profile, mfaSettings, editProfile);
        }

        return navbar;
    }

    // attach both header bar and nav bar
    protected void attachHeaderAndNavBar(int type, String title) {
        // grab border pane
        BorderPane pane = (BorderPane) getRootPane();

        // define headerbar & navbar
        HeaderBar headerBar = null;
        NavBar navBar = null;

        // if type = 1
        if (type == 1) {
            // create header bar
            headerBar = createHeaderBar(1, "Profile", "/images/user.png", title);

            // create nav bar
            navBar = createNavBar(1);
        }
        // if type = 2
        else if (type == 2) {
            // create header bar
            headerBar = createHeaderBar(2, "Home", "/images/home.png", title);

            // create nav bar
            navBar = createNavBar(2);
        }
        // otherwise, invalid type
        else {
            logger.error("Invalid type: " + type + " passed into method, when attaching header and nav bar.");
            throw new IllegalArgumentException(
                "Invalid type: " + type + " passed into method, when attaching header and nav bar."
            );
        }

        // make new VBox with header bar and nav bar
        VBox headerAndNav = new VBox(headerBar, navBar);

        // set header bar AND nav bar to the top of the border pane
        pane.setTop(headerAndNav);
    }

    /**
     * Attach default pane to screen
     *
     * @param previousRootPane Pane
     */
    protected void addBackAndHomeButtons(Pane previousRootPane) {
        // Create a StackPane to layer the button on top of the BorderPane
        StackPane stackPane = new StackPane();
        // make stack pane the root pane
        setRootPane(stackPane);
        // add border pane to stack pane
        stackPane.getChildren().add(previousRootPane);

        // Add back button
        addBackButton(stackPane);
        // Add home button
        addHomeButton(stackPane);

        // add CSS to stackpane
        this.addCss("/css/abstract.css");
        stackPane.getStyleClass().add("stackpane");
    }

    /**
     * Add back button to screen
     *
     * @param stackPane StackPane
     */
    protected void addBackButton(StackPane stackPane) {
        // Make icon button
        ImageView img = new ImageView(new Image("/images/back.png"));
        img.preserveRatioProperty().set(true);
        img.setFitHeight(100);
        img.setFitWidth(100);

        // Make box for storing the image
        HBox box = new HBox(img);

        // on click, go back to the previous screen
        box.setOnMouseClicked(e -> {
            getScreenManager().goBack();
            logger.info("Back button clicked");
        });

        // Add it to the top left of the StackPane with absolute positioning (the latest
        // children are placed on top sequentially, back to front)
        StackPane.setAlignment(box, Pos.TOP_LEFT);
        stackPane.getChildren().add(box);

        // Load & Apply CSS
        this.addCss("/css/abstract.css");
        img.getStyleClass().add("back-button");
        box.getStyleClass().add("back-button-box");
    }

    /**
     * Add home button to screen
     *
     * @param stackPane StackPane
     */
    protected void addHomeButton(StackPane stackPane) {
        // Make icon button
        ImageView img = new ImageView(new Image("/images/home.png"));
        img.preserveRatioProperty().set(true);
        img.setFitHeight(100);
        img.setFitWidth(100);

        // Make box for storing the image
        HBox box = new HBox(img);

        // on click, go back to the home screen
        box.setOnMouseClicked(e -> {
            logger.info("Home button clicked");
            // if user is authenticated, go to home screen
            if (SessionManager.getInstance().isAuthenticated()) {
                showScene(HomeScreen.class);
            }
            // if user is not authenticated, go to welcome screen
            else {
                showScene(WelcomeScreen.class);
            }
        });

        // Add it to the top right of the StackPane with absolute positioning (the
        // latest
        // children are placed on top sequentially, back to front)
        StackPane.setAlignment(box, Pos.TOP_RIGHT);
        stackPane.getChildren().add(box);

        // Load & Apply CSS
        this.addCss("/css/abstract.css");
        img.getStyleClass().add("home-button");
        box.getStyleClass().add("home-button-box");
    }

    /**
     * Shortcut to show a scene
     *
     * @param screenClass Class
     */
    protected void showScene(Class<? extends AbstractScreen> screenClass) {
        getApp().getScreenManager().showScene(screenClass);
    }

    /**
     * Shortcut to refresh the scene
     */
    protected void refreshScene() {
        getApp().getScreenManager().refreshScene();
    }

    /**
     * Shortcut to show screen between screens with a specific delay
     *
     * @param msg             The message to display
     * @param nextScreenClass The next screen to show
     * @param delay           The delay (in ms) to wait before showing the next
     *                        screen
     */
    protected void showSceneBetweenScenesThenNextScene(
        String msg,
        Class<? extends AbstractScreen> nextScreenClass,
        int delay
    ) {
        try {
            logger.info("Showing ScreenBetweenScreens screen, with message: " + msg);

            // set session manager state message
            SessionManager.getInstance().setStateMessage(msg);

            // show the ScreenBetweenScreens screen
            this.showScene(ScreenBetweenScreens.class);

            // pause transition betfore moving to next screen
            PauseTransition pause = new PauseTransition(Duration.millis(delay));
            pause.setOnFinished(event -> {
                showScene(nextScreenClass);
            });
            pause.play();
        } catch (Exception e) {
            logger.error("Error in showSceneBetweenScenesThenNextScene: " + e.getMessage());
            logger.info("Redirecting to LoginScreen screen immediately");
            showScene(LoginScreen.class);
        }
    }

    /**
     * Shortcut to show a scene
     *
     * @param msg             String
     * @param nextScreenClass Class
     */
    protected void showSceneBetweenScenesThenNextScene(String msg, Class<? extends AbstractScreen> nextScreenClass) {
        this.showSceneBetweenScenesThenNextScene(msg, nextScreenClass, AppConfig.TIMEOUT_MS);
    }
}
