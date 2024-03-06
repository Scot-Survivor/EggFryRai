package com.comp5590;

import com.comp5590.configuration.AppConfig;
import com.comp5590.entities.Patient;
import com.comp5590.managers.DatabaseManager;
import com.comp5590.managers.ScreenManager;
import com.comp5590.managers.security.mfa.TOTPManager;
import com.comp5590.managers.security.passwords.Argon2PasswordManager;
import com.comp5590.managers.security.passwords.PasswordManager;
import com.comp5590.screens.HomeScreen;
import com.comp5590.screens.LoginScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class App extends Application {
    private static App instance;
    private DatabaseManager databaseManager;
    private AppConfig appConfig;
    @Getter
    private PasswordManager passwordManager;
    @Getter
    private ScreenManager screenManager;
    @Getter
    private TOTPManager totpManager;

    @Setter
    @Getter
    private Patient currentUser;

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
        instance = this;
        primaryStage = stage;
        appConfig = AppConfig.getInstance();
        screenManager = new ScreenManager(primaryStage);
        totpManager = TOTPManager.getInstance();

        databaseManager = DatabaseManager.getInstance();

        passwordManager = new Argon2PasswordManager();
    }

    public static void main(String[] args) {
        launch();
    }

    public DatabaseManager getDatabase() {
        return databaseManager;
    }

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }
}
