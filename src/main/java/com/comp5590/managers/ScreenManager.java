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
import com.comp5590.screens.LoginScreen;
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
        if (accessedScreens.size() >= 5) {
            accessedScreens.remove(0);
        }
        accessedScreens.add(clazz);
    }

    /**
     * Go back to the previous screen
     * If there is no previous screen, do nothing
     */
    public void goBack() {
        if (accessedScreens.size() > 1) {
            Class<? extends AbstractScreen> lastScreen = accessedScreens.get(accessedScreens.size() - 2);
            showScene(lastScreen);
            accessedScreens.remove(accessedScreens.size() - 1);
        }
    }

    public AbstractScreen getScreenInstance(Class<? extends AbstractScreen> screen) {
        return screenInstances.get(screen);
    }

    // is between screens
    public boolean isBetweenScreens(Class<? extends AbstractScreen> screen) {
        return screen.getClass().getSimpleName().toLowerCase().startsWith("screenbetweenscreens");
    }

    // get previous screen
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
