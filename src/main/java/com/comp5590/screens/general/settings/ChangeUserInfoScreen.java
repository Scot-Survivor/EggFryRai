package com.comp5590.screens.general.settings;

import com.comp5590.components.LoginScreen.Title;
import com.comp5590.managers.LoggerManager;
import com.comp5590.screens.managers.ScreenManager;
import com.comp5590.screens.misc.AbstractScreen;
import com.comp5590.security.authentication.annotations.AuthRequired;
import javafx.geometry.HPos;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.core.Logger;

@AuthRequired
public class ChangeUserInfoScreen extends AbstractScreen {

    private final Logger logger = LoggerManager.getInstance().getLogger(ChangeUserInfoScreen.class);

    public ChangeUserInfoScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // Load CSS
        // this.addCss("/css/global.css");

        // attach default pane, but grab the reference to the gridpane (set as
        // center of borderpane) for further customization
        GridPane gridPane = this.attachDefaultPane();

        // attach the header bar & navbar (type 2, cos we're in the profile / settings
        // area)
        this.attachHeaderAndNavBar(2, "Edit your profile");

        // add boilerplate msg to center of screen
        Title title = new Title("TO BE COMPLETE...");
        gridPane.add(title, 0, 0);
        GridPane.setHalignment(title, HPos.CENTER);
    }

    @Override
    public void cleanup() {
        // nothing to clean up
    }
}
