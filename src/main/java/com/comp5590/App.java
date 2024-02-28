package com.comp5590;

/**
 * File responsible for loading and unloading of scenes
 */

import com.comp5590.configuration.AppConfig;
import com.comp5590.managers.DatabaseManager;
import com.comp5590.managers.SceneManager;
import com.comp5590.screens.HomeScreen;
import com.comp5590.screens.LoginScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private DatabaseManager db;
    private AppConfig appConfig;
// private PasswordManager pm; TODO: Fix password manager.

    private Stage primaryStage;

    // scenes and screen objects
    private LoginScreen loginScreen;
    private HomeScreen homeScreen;
    private Scene loginScene;
    private Scene homeScene;

    // default sizes for the screen
    private static final int height = 300;
    private static final int width = 300;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        appConfig = AppConfig.getInstance();
        SceneManager sceneManager = new SceneManager(primaryStage);

        db = DatabaseManager.getInstance();
        //TODO: Fix password manager
//        pm = new ArgonPasswordManager();  // TODO: Write a factory setup for password managers.
    }

    public static void main(String[] args) {
        launch();
    }

    public DatabaseManager getDatabase() {
        return db;
    }

    //TODO: Fix password manager.
//    public PasswordManager getPm() {
//        return pm;
//    }
}
