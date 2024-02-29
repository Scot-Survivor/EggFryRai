package com.comp5590.screens;

import com.comp5590.managers.SceneManager;
import javafx.scene.layout.Pane;

public abstract class AbstractScreen {
    private final SceneManager sceneManager;
    private Pane rootPane;
    protected String cssPath;
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

    public String getCssPath() {
        return cssPath;
    }


}
