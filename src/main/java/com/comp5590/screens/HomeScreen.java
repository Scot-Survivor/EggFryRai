package com.comp5590.screens;

import com.comp5590.components.HomeScreen.BackgroundImage;
import com.comp5590.components.HomeScreen.HeaderBar;
import com.comp5590.components.HomeScreen.NavBar;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import com.comp5590.security.managers.authentication.annotations.AuthRequired;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import org.apache.logging.log4j.core.Logger;

@AuthRequired
public class HomeScreen extends AbstractScreen {

    private final Logger logger = LoggerManager.getInstance().getLogger(HomeScreen.class);

    public HomeScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // Load CSS
        this.addCss("/home.css");

        GridPane pane = new GridPane();
        pane.getStyleClass().add("custom-pane");

        // Initialize name to "Guest" by default
        String name = "Guest";

        // TODO: Fix this shit (lol)
        // Check if the App instance is available
        if (getApp() != null && getApp().getCurrentUser() != null) {
            // Retrieve the user's first name if available
            name = getApp().getCurrentUser().getFirstName();
        }

        // create hbox for the logout event listener
        HBox logoutBox = new HBox();

        // attach event listener to logoutbox
        logoutBox.setOnMouseClicked(e -> {
            this.showSceneBetweenScenesThenNextScene(
                    "👋 You have successfully logged out.\nRedirecting to login screen...",
                    3,
                    LoginScreen.class
                );
        });

        // Create the header bar with the determined name
        HeaderBar headerBar = new HeaderBar(name, logoutBox);

        // create Buttons for each navbar item
        Button home = new Button("Home");
        Button appointments = new Button("Appointments");
        Button prescriptions = new Button("Prescriptions");
        Button aboutUs = new Button("About us");
        Button contactUs = new Button("Contact us");
        Button doctors = new Button("Doctors");

        // attach event listeners to each button
        home.setOnAction(e -> {
            logger.info("Home button clicked");
            showScene(HomeScreen.class);
        });

        appointments.setOnAction(e -> {
            logger.info("Appointments button clicked");
            // showScene(AppointmentsScreen.class);
        });

        prescriptions.setOnAction(e -> {
            logger.info("Prescriptions button clicked");
            // showScene(PrescriptionsScreen.class);
        });

        aboutUs.setOnAction(e -> {
            logger.info("About us button clicked");
            // showScene(AboutUsScreen.class);
        });

        contactUs.setOnAction(e -> {
            logger.info("Contact us button clicked");
            // showScene(ContactUsScreen.class);
        });

        doctors.setOnAction(e -> {
            logger.info("Doctors button clicked");
            showScene(DocListScreen.class);
        });

        // create the navbar
        NavBar navBar = new NavBar(home, appointments, prescriptions, aboutUs, contactUs, doctors);

        // create the background image
        BackgroundImage bgImg = new BackgroundImage("/homeBackground.jpg");

        // add the header bar to the 1st row of the pane
        pane.add(headerBar, 0, 0);
        // span the header bar across the entire width of the pane (infinite)
        GridPane.setColumnSpan(headerBar, Integer.MAX_VALUE);
        // add the navbar to the 2nd row of the pane
        pane.add(navBar, 0, 1);
        // span the navbar across the entire width of the pane (infinite)
        GridPane.setColumnSpan(navBar, Integer.MAX_VALUE);
        // add the background image to the 3rd row of the pane
        pane.add(bgImg, 0, 2);
        // span the background image across the entire width & height of the pane
        // (infinite)
        GridPane.setColumnSpan(bgImg, Integer.MAX_VALUE);
        GridPane.setRowSpan(bgImg, Integer.MAX_VALUE);

        // add column constraints, so its width is always width of the screen
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(100);
        pane.getColumnConstraints().add(col1);

        // create the border pane (which will serve as root pane)
        // set grid pane as child of border pane
        BorderPane rootPane = new BorderPane();
        rootPane.setPadding(new Insets(0, 0, 0, 0));
        rootPane.setTop(pane);

        setRootPane(rootPane); // set root pane
    }
}
