package com.comp5590.screens;

import com.comp5590.components.RegisterScreen.BackToLoginBox;
import com.comp5590.components.WelcomeScreen.ContinueWithEmailBox;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.core.Logger;

public class WelcomeScreen extends AbstractScreen {

    private final Logger logger = LoggerManager.getInstance().getLogger(HomeScreen.class);

    public WelcomeScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // load custom CSS
        this.addCss("/css/welcome.css");

        // create the grid pane object, apply styling & properties to it
        GridPane pane = new GridPane();
        pane.getStyleClass().add("custom-pane");

        // create child components
        Text text1 = new Text("Welcome to ");
        text1.getStyleClass().add("welcome-text-one");

        Text text2 = new Text("GP Alpha");
        text2.getStyleClass().add("welcome-text-two");

        Text text3 = new Text("We are here for you.");
        text3.getStyleClass().add("welcome-text-three");

        // create more child components
        Text text4 = new Text("Let's get started...");
        text4.getStyleClass().add("welcome-text-four");

        ContinueWithEmailBox continueWithEmail = new ContinueWithEmailBox();
        BackToLoginBox backToLogin = new BackToLoginBox();

        // bind event listeners to above components
        continueWithEmail.setOnMouseClicked(e -> {
            logger.info("Continue with email button clicked");
            showScene(RegisterScreen.class);
        });

        backToLogin.setOnMouseClicked(e -> {
            logger.info("Back to login button clicked");
            showScene(LoginScreen.class);
        });

        // add above child components 2 VBoxes (2 columns)
        VBox vbox1 = new VBox();
        vbox1.getChildren().addAll(text1, text2, text3);
        vbox1.getStyleClass().add("vbox1");

        VBox vbox2 = new VBox();
        vbox2.getChildren().addAll(text4, continueWithEmail, backToLogin);
        vbox2.getStyleClass().add("vbox2");

        // add child components to the grid pane
        pane.add(vbox1, 1, 0);
        pane.add(vbox2, 1, 1);

        // put the grid pane in the middle of a border pane
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pane);
        borderPane.setId("background-img");

        // set border pane as root pane
        setRootPane(borderPane);
    }

    @Override
    public void cleanup() {
        // nothing to clean up
    }
}
