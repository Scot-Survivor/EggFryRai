package com.comp5590;

import com.comp5590.database.Database;
import com.comp5590.secuirty.ArgonPasswordManager;
import com.comp5590.secuirty.PasswordManager;
import com.comp5590.scenes.Login;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class HelloFX extends Application {
    private Database db;
// private PasswordManager pm; TODO: Fix password manager.

    private Stage primaryStage;

    // scenes and screen objects
    private Login loginScreen;
    private Scene loginScene;

    // default sizes for the screen
    private static final int height = 300;
    private static final int width = 300;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;

        // create a new login screen with this object
        loginScreen = new Login(this);
        createLoginScene();
        showLoginScreen();

        // get the screen and size and set to be full screen borderless
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        db = new Database();
        //TODO: Fix password manager
//        pm = new ArgonPasswordManager();  // TODO: Write a factory setup for password managers.
    }

    /**
     * Create the new login screen
     */
    private void createLoginScene() {
        loginScene = new Scene(loginScreen.getRoot(), width, height);
        loginScene.getStylesheets().add(getClass().getResource("/login.css").toExternalForm());
    }

    /**
     * Function that shows the login screen
     */
    public void showLoginScreen() {
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login Screen");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch();
    }

    public Database getDatabase() {
        return db;
    }

    //TODO: Fix password manager.
//    public PasswordManager getPm() {
//        return pm;
//    }
}
