/**
 * Will take in any scene and then take that and display it
 * @since 2024-02-28
 */

package com.comp5590.managers;

import com.comp5590.App;
import com.comp5590.events.eventtypes.CancellableEvent;
import com.comp5590.events.eventtypes.screens.ScreenChangeEvent;
import com.comp5590.events.managers.EventManager;
import com.comp5590.screens.AbstractScreen;
import com.comp5590.screens.WelcomeScreen;
import java.util.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.logging.log4j.core.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class ScreenManager {

    private Stage primaryStage;

    // default sizes for the screen
    private static final int height = 300;
    private static final int width = 300;

    @Getter
    private final HashMap<Class<? extends AbstractScreen>, AbstractScreen> screenInstances;

    private final Logger logger = LoggerManager.getInstance().getLogger(ScreenManager.class);

    @Getter
    private final List<Class<? extends AbstractScreen>> accessedScreens;

    @Getter
    private AbstractScreen currentScreen;

    @Getter
    private final EventManager eventManager;

    @Getter
    private final App app;

    public ScreenManager(Stage primary) {
        this.primaryStage = primary;
        this.screenInstances = new HashMap<>();
        this.eventManager = EventManager.getInstance();
        this.app = App.getInstance();
        this.accessedScreens = new ArrayList<>();

        // run any setup functions and then display the login scene
        setup();
        // ! ALWAYS CHANGE BACK TO WELCOME SCREEN BEFORE COMMITS / PRs. Only use other
        // ! screens during testing.
        showScene(WelcomeScreen.class);
        fullscreen();
    }

    /**
     * Short hand method to check if the event should be cancelled
     *
     * @param event The event to check
     * @return If the event should be cancelled
     */
    private boolean shouldCancel(CancellableEvent event) {
        return event.isCancelled();
    }

    /**
     * Run any setup that is needed. i.e adding all of the scenes to the HashMap
     */
    private void setup() {
        Reflections reflections = new Reflections("com.comp5590.screens", Scanners.SubTypes);
        for (Class<? extends AbstractScreen> screen : reflections.getSubTypesOf(AbstractScreen.class)) {
            try {
                AbstractScreen instance = screen.getConstructor(ScreenManager.class).newInstance(this);
                screenInstances.put(screen, instance);
            } catch (Exception e) {
                logger.error("Error creating instance of screen: {} | Reason: {}", screen.getName(), e.getMessage());
                logger.debug(Arrays.toString(e.getStackTrace()));
            }
        }
        logger.debug("All {} screens have been added to the scene manager", screenInstances.size());
    }

    /**
     * Set the screen to be full screen
     */
    public void fullscreen() {
        // get the screen and size and set to be full screen borderless
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
    }

    /**
     * Function that shows the scene
     * Example:
     * showScene(Login.class);
     *
     * @param scene The scene to show
     */
    public void showScene(Class<? extends AbstractScreen> scene) {
        if (screenInstances.containsKey(scene)) {
            if (shouldCancel(eventManager.callEvent(new ScreenChangeEvent(screenInstances.get(scene), app)))) {
                logger.debug("ScreenChangeEvent was cancelled for: " + scene.getName());
                return;
            }
            addScreenToHistory(scene);

            // check scene is not currently showing
            if (this.currentScreen == screenInstances.get(scene)) {
                logger.warn("Scene {} is already showing", scene.getName());
            } else {
                if (this.currentScreen != null) {
                    // Cleanup the old screen
                    this.currentScreen.cleanup();
                }
                // grab the current screen and clean it up
                this.currentScreen = screenInstances.get(scene);
                if (this.currentScreen != null) {
                    // Set up the new one
                    this.currentScreen.setup();
                }
                Scene toShow = createScene(screenInstances.get(scene));
                // display the new scene.
                primaryStage.setScene(toShow);
                primaryStage.setTitle("PDMS");
                primaryStage.show();
            }
        } else {
            logger.error("Scene {} not found", scene.getName());
            logger.debug("Available scenes: {}", screenInstances.keySet());
        }
    }

    public void refreshScene() {
        if (this.currentScreen != null) {
            this.currentScreen.cleanup();
            this.currentScreen.setup();
            Scene toShow = createScene(this.currentScreen);
            primaryStage.setScene(toShow);
            primaryStage.setTitle("PDMS");
            primaryStage.show();
        }
    }

    private Scene createScene(AbstractScreen screen) {
        Scene scene = new Scene(screen.getRootPane(), width, height);
        for (String path : screen.getCssPaths()) {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(path)).toExternalForm());
        }
        return scene;
    }

    /**
     * Add the screen to the history
     *
     * @param clazz The class of the screen to add
     */
    private void addScreenToHistory(Class<? extends AbstractScreen> clazz) {
        // if is ScreenBetweenScreens, do not add to history
        if (isBetweenScreens(clazz)) {
            return;
        }
        // if the accessed screens is greater than 5, remove the first element
        if (accessedScreens.size() >= 5) {
            accessedScreens.remove(0);
        }
        // add the screen to the accessed screens
        accessedScreens.add(clazz);
    }

    /**
     * Go back to the previous screen
     * If there is no previous screen, do nothing
     */
    public void goBack() {
        if (accessedScreens.size() > 1) {
            // remove the current screen
            accessedScreens.remove(accessedScreens.size() - 1);
            // get the previous screen
            Class<? extends AbstractScreen> previous = accessedScreens.get(accessedScreens.size() - 1);
            // show the previous screen
            showScene(previous);
        }
    }

    /**
     * Get the instance of the screen
     *
     * @param T The screen to get the instance of
     * @return The instance of the screen
     */
    public <T> T getScreenInstance(Class<? extends AbstractScreen> T) {
        return (T) screenInstances.get(T);
    }

    /**
     * Check if the screen is a ScreenBetweenScreens
     *
     * @param screen The screen to check
     * @return If the screen is a ScreenBetweenScreens
     */
    public boolean isBetweenScreens(Class<? extends AbstractScreen> screen) {
        // logger.info("Checking if screen is between screens: {}",
        // screen.getSimpleName());
        return screen.getSimpleName().toLowerCase().equals("screenbetweenscreens");
    }

    /**
     * Get the previous screen
     *
     * @return The previous screen
     */
    public AbstractScreen getPreviousScreen() {
        // compensate for a potential ScreenBetweenScreens at the previous index. If so,
        // return the screen before that.
        if (accessedScreens.size() > 1) {
            if (isBetweenScreens((accessedScreens.get(accessedScreens.size() - 2)))) {
                return getScreenInstance(accessedScreens.get(accessedScreens.size() - 3));
            }
            return getScreenInstance(accessedScreens.get(accessedScreens.size() - 2));
        }

        return null;
    }
}
