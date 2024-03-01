package com.comp5590;

/**
 * File responsible for loading and unloading of scenes
 */

import com.comp5590.configuration.AppConfig;
import com.comp5590.managers.DatabaseManager;
import com.comp5590.managers.ScreenManager;
import com.comp5590.managers.security.passwords.Argon2PasswordManager;
import com.comp5590.managers.security.passwords.PasswordManager;
import com.comp5590.screens.HomeScreen;
import com.comp5590.screens.LoginScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private DatabaseManager db;
    private AppConfig appConfig;
    private PasswordManager pm;

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
        ScreenManager screenManager = new ScreenManager(primaryStage);

        db = DatabaseManager.getInstance();

        pm = new Argon2PasswordManager();
    }

    public static void main(String[] args) {
        launch();
    }

    public DatabaseManager getDatabase() {
        return db;
    }

    public PasswordManager getPm() {
        return pm;
    }
}
