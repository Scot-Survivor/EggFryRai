package com.comp5590.events.listeners.implementations;

import com.comp5590.components.LoginScreen.BigButton;
import com.comp5590.events.listeners.interfaces.KeyboardListener;
import com.comp5590.managers.LoggerManager;
import com.comp5590.screens.managers.ScreenManager;
import com.comp5590.screens.misc.AbstractScreen;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.core.Logger;

/**
 * Listener to handle keyboard navigation on screens.
 */
public class SceneKeyboardNavigationListener implements KeyboardListener {

    private ScreenManager screenManager;
    private Stage primaryStage;
    private final Logger logger = LoggerManager.getInstance().getLogger(SceneKeyboardNavigationListener.class);

    public SceneKeyboardNavigationListener(ScreenManager screenManager, Stage stage) {
        this.screenManager = screenManager;
        this.primaryStage = stage;

        // add event filter to scene (for global enter key press)
        stage
            .getScene()
            .addEventFilter(
                KeyEvent.KEY_RELEASED,
                event -> {
                    // if key is Enter
                    if (event.getCode() == KeyCode.ENTER) {
                        // grab current screen
                        AbstractScreen currentScreen = this.getCurrentScreen();
                        // grab screen name
                        String screenName = this.getScreenName();
                        // get root pane
                        Pane rootPane = this.getRootPane();

                        logger.debug("Enter key pressed on screen: " + screenName);

                        // if screen is ScreenBetweenScreens, drop the event
                        if (isBetweenScreens(currentScreen)) {
                            return;
                        }

                        // if the screen is LoginScreen
                        if (isScreen(currentScreen, "login")) {
                            // grab button of ID "login"
                            BigButton loginButton = (BigButton) rootPane.lookup("#login");

                            // simulate button click
                            loginButton.fire();
                        }
                        // if screen is register screen
                        else if (isScreen(currentScreen, "register")) {
                            // grab button of ID "registerButton"
                            BigButton registerButton = (BigButton) rootPane.lookup("#registerButton");

                            // simulate button click
                            registerButton.fire();
                        }
                    }

                    event.consume(); // Consume the event to keep the filter attached
                }
            );

        // add event filter to certain nodes (for global backspace / ESC key press)
        stage
            .getScene()
            .addEventFilter(
                KeyEvent.KEY_RELEASED,
                event -> {
                    // if key is backspace or ESC
                    if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.ESCAPE) {
                        // get previous screen (this method also compensates for ScreenBetweenScreens,
                        // so not to worry about that)
                        AbstractScreen previousScreen = screenManager.getPreviousScreen();

                        // check if previous screen exists, if not, return
                        if (previousScreen == null) {
                            return;
                        }

                        // grab screen name
                        String screenName = this.getScreenName();

                        logger.debug("Backspace / ESC key pressed on screen: " + screenName);

                        // go back to previous screen, gracefully.
                        screenManager.showScene(previousScreen.getClass());
                    }

                    event.consume(); // Consume the event to keep the filter attached
                }
            );
    }

    /**
     * Check if the screen is of a certain type
     * @param screen The screen to check
     * @param screenName The screen name to check
     * @return If the screen is of the type
     */
    private boolean isScreen(AbstractScreen screen, String screenName) {
        return screen.getClass().getSimpleName().toLowerCase().startsWith(screenName);
    }

    /**
     * Check if the screen is a ScreenBetweenScreens
     * @param screen The screen to check
     * @return If the screen is a ScreenBetweenScreens
     */
    private boolean isBetweenScreens(AbstractScreen screen) {
        return screen.getClass().getSimpleName().toLowerCase().startsWith("screenbetweenscreens");
    }

    /**
     * Get the current screen
     * @return The current screen
     */
    private AbstractScreen getCurrentScreen() {
        return screenManager.getCurrentScreen();
    }

    /**
     * Get the root pane of the current screen
     * @return The root pane
     */
    private Pane getRootPane() {
        return getCurrentScreen().getRootPane();
    }

    /**
     * Get the screen name
     * @return The screen name
     */
    private String getScreenName() {
        return getCurrentScreen().getClass().getSimpleName().toLowerCase();
    }
}
