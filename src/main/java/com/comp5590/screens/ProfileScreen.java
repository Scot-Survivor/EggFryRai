package com.comp5590.screens;

import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import com.comp5590.security.managers.authentication.annotations.AuthRequired;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.core.Logger;

@AuthRequired
public class ProfileScreen extends AbstractScreen {

    private final Logger logger = LoggerManager.getInstance().getLogger(ProfileScreen.class);

    public ProfileScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        this.addCss("/profile.css");

        // attach default pane, but grab the grid pane for adding components
        GridPane pane = attachDefaultPane();
        // create child components
    }

    @Override
    public void cleanup() {}
}
