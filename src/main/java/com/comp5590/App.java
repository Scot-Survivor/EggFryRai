package com.comp5590;

import com.comp5590.configuration.AppConfig;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.events.listeners.implementations.EntityValidatorListener;
import com.comp5590.events.listeners.implementations.SceneKeyboardNavigationListener;
import com.comp5590.events.managers.EventManager;
import com.comp5590.managers.ScreenManager;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.HomeScreen;
import com.comp5590.screens.LoginScreen;
import com.comp5590.security.managers.authentication.listeners.ScreenAuthValidationListener;
import com.comp5590.security.managers.mfa.TOTPManager;
import com.comp5590.security.managers.passwords.Argon2PasswordManager;
import com.comp5590.security.managers.passwords.PasswordManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;

public class App extends Application {

    private static App instance;
    private DatabaseManager databaseManager;
    private AppConfig appConfig;

    @Getter
    private EventManager eventManager;

    @Getter
    private PasswordManager passwordManager;

    @Getter
    private ScreenManager screenManager;

    @Getter
    private TOTPManager totpManager;

    @Getter
    private SessionManager sessionManager;

    private User currentUser;

    @Getter
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
        // instantiate managers & everything else programatically
        sessionManager = SessionManager.getInstance();
        databaseManager = DatabaseManager.getInstance();
        passwordManager = new Argon2PasswordManager();

        // instantiate other fields
        instance = this;
        primaryStage = stage;
        stage.getIcons().add(new Image("/healthcare.png"));
        appConfig = AppConfig.getInstance();
        screenManager = new ScreenManager(primaryStage);
        totpManager = TOTPManager.getInstance();
        // add event listeners to the event manager
        eventManager = EventManager.getInstance();
        if (AppConfig.DO_ENTITY_VALIDATION) {
            eventManager.addListener(new EntityValidatorListener());
        }
        eventManager.addListener(new ScreenAuthValidationListener());
        eventManager.addListener(new SceneKeyboardNavigationListener(screenManager, primaryStage));
    }

    public static void main(String[] args) {
        launch();
    }

    public DatabaseManager getDatabase() {
        return databaseManager;
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    @Deprecated
    public User getCurrentUser() {
        return SessionManager.getInstance().getCurrentUser();
    }

    @Deprecated
    public void setCurrentUser(User user) {
        SessionManager.getInstance().setCurrentUser(user);
    }
}
