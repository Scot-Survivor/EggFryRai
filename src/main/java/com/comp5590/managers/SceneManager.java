/**
 * Will take in any scene and then take that and display it
 * @since 2024-02-28
 */

package com.comp5590.managers;

import com.comp5590.screens.AbstractScreen;
import com.comp5590.screens.HomeScreen;
import com.comp5590.screens.LoginScreen;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.HashMap;

public class SceneManager {

    private Stage primaryStage;

    // default sizes for the screen
    private static final int height = 300;
    private static final int width = 300;

    private final HashMap<Class<? extends AbstractScreen>, Scene> screens;

    public SceneManager(Stage primary){
        this.primaryStage = primary;
        this.screens = new HashMap<>();

        // run any setup functions and then display the login scene
        setup();
        showScene(LoginScreen.class);
        fullscreen();
    }

    /**
     * Run any setup that is needed. i.e adding all of the scenes to the HashMap
     */
    private void setup() {
        screens.put(LoginScreen.class, createScene("/login.css", new LoginScreen(this)));
        screens.put(HomeScreen.class, createScene(null, new HomeScreen(this)));
    }

    /**
     * Set the screen to be full screen
     */
    public void fullscreen(){
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
     *  showScene(Login.class);
     * @param scene The scene to show
     */
    public void showScene(Class<? extends AbstractScreen> scene) {
        if (screens.containsKey(scene)) {
            Scene toShow = screens.get(scene);
            // check scene is not currently showing
            if (primaryStage.getScene() == toShow){
                System.out.println("Scene currently showing");
            }
            else{
                // display the new scene.
                primaryStage.setScene(toShow);
                primaryStage.setTitle("PDMS");
                primaryStage.show();
            }
        } else {
            throw new RuntimeException("Scene has not been created in %s::setup"
                    .formatted(this.getClass().getName() + ".java")
            );
        }
    }

    private Scene createScene(String cssPath, AbstractScreen screen){
        Scene scene = new Scene(screen.getRootPane(), width, height);
        if (cssPath != null)
            scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        return scene;
    }
}
