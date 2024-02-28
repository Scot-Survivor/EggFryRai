package com.comp5590.managers;

/**
 * Will take in any scene and then take that and display it
 * @author Rhys Walker
 * @since 2024-02-28
 */

import com.comp5590.scenes.Home;
import com.comp5590.scenes.Login;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SceneManager {

    private Stage primaryStage;
    // create screen objects
    private Login loginScreen;
    private Home homeScreen;
    // create scene objects
    private Scene loginScene;
    private Scene homeScene;

    // default sizes for the screen
    private static final int height = 300;
    private static final int width = 300;
    public SceneManager(Stage primary){
        this.primaryStage = primary;

        // create all the scenes here
        createLoginScene();
        createHomeScene();

        // by default show the login screen
        showLoginScreen();

        // get the screen and size and set to be full screen borderless
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
    }

    /**
     * Create the new login screen
     */
    private void createLoginScene() {
        loginScreen = new Login(this);
        loginScene = new Scene(loginScreen.getRootPane(), width, height);
        // apply css to the scene
        loginScene.getStylesheets().add(getClass().getResource("/login.css").toExternalForm());
    }

    /**
     * Create the new home screen
     */
    private void createHomeScene() {
        homeScreen = new Home(this);
        homeScene = new Scene(homeScreen.getRootPane(), width, height);
    }

    /**
     * Function that shows the login screen
     */
    public void showLoginScreen() {
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login Screen");
        primaryStage.show();
    }

    /**
     * Function that shows the home screen
     */
    public void showHomeScreen() {
        primaryStage.setScene(homeScene);
        primaryStage.setTitle("Home Screen");
        primaryStage.show();
    }
}
