package com.comp5590.screens;

import com.comp5590.managers.SceneManager;
import javafx.scene.layout.Pane;

public abstract class AbstractScreen {
    SceneManager sceneManager;
    Pane rootPane;
    public AbstractScreen(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.setup();
    }

    public abstract void setup();

    public void setRootPane(Pane root) {
        this.rootPane = root;
    }

    public Pane getRootPane() {
        return rootPane;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }


}
