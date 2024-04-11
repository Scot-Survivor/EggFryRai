package com.comp5590.screens;

import com.comp5590.components.HomeScreen.HugeImage;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import com.comp5590.security.managers.authentication.annotations.AuthRequired;
import javafx.scene.layout.*;
import org.apache.logging.log4j.core.Logger;

@AuthRequired
public class HomeScreen extends AbstractScreen {

    private final Logger logger = LoggerManager.getInstance().getLogger(HomeScreen.class);

    public HomeScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // Load CSS
        this.addCss("/home.css");
        this.addCss("/global.css");

        // attach default pane, but grab the reference to the gridpane (set as
        // center of borderpane) for further customization
        GridPane pane = this.attachDefaultPane();

        // attach the header bar & navbar
        this.attachHeaderAndNavBar("GP Alpha");

        // create the background image
        HugeImage bgImage = new HugeImage("/homeBackground.jpg");

        // set the background image to the 3rd row of the pane, and span it infinitely
        // across the width and height of the pane (dynamic sizing)
        pane.add(bgImage, 0, 2);
        // add column constraints, so its width is always width of the screen
        ColumnConstraints col1 = new ColumnConstraints();

        col1.setPercentWidth(100);
        pane.getColumnConstraints().add(col1);
    }

    @Override
    public void cleanup() {
        // nothing to clean up
    }
}
