package com.comp5590.screens;

import com.comp5590.entities.Patient;
import com.comp5590.managers.SceneManager;
import javafx.scene.layout.Pane;

public abstract class AbstractScreen {
    private final SceneManager sceneManager;
    private Pane rootPane;
    /**
     * The path to the CSS file for the screen
     */
    protected String cssPath;
    public AbstractScreen(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.setup();
    }

    /**
     * This method is called when the screen is created
     */
    public abstract void setup();

    /**
     * This method is called when the screen is shown, to ensure that the logged in user has access to the screen
     * @return true if the user has access to the screen
     */
    public boolean canAccess() {
        return true;
    };

    /**
     * This method is called when the screen is shown, to ensure that the logged in user has access to the screen
     * @param patient object
     * @return true if the user has access to the screen
     */
    public boolean canAccess(Patient patient) {
        return true;
    };

    /**
     * Set the root pane for the screen
     * @param root the root pane
     */
    public void setRootPane(Pane root) {
        this.rootPane = root;
    }

    /**
     * Get the root pane for the screen
     * @return the root pane
     */
    public Pane getRootPane() {
        return rootPane;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public String getCssPath() {
        return cssPath;
    }


}
