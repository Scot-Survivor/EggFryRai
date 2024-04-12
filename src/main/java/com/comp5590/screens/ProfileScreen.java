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

        // attach default pane, but grab the reference to the gridpane (set as
        // center of borderpane) for further customization
        GridPane pane = this.attachDefaultPane();
        // attach header and nav bar
        this.attachHeaderAndNavBar("Your Profile");
    }

    @Override
    public void cleanup() {}
}
