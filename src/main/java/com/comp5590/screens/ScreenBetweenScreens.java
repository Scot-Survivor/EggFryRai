package com.comp5590.screens;

import com.comp5590.components.LoginScreen.Title;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.core.Logger;

public class ScreenBetweenScreens extends AbstractScreen {

    private final Logger logger = LoggerManager.getInstance().getLogger(ScreenBetweenScreens.class);

    public ScreenBetweenScreens(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // load css
        this.addCss("/screenBetweenScreens.css");

        GridPane pane = new GridPane();
        pane.getStyleClass().add("custom-pane");

        Title title = new Title("Boilerplate Message");
        title.getStyleClass().add("message");

        // add the title to the pane
        pane.add(title, 0, 0);

        // set root pane
        setRootPane(pane);
    }

    public void runFunctionalityAfterDisplayingScene(String msg) {
        // log the message
        logger.info(msg);

        // load css
        this.addCss("/screenBetweenScreens.css");

        // get the title from the root pane
        Title title = (Title) ((GridPane) getRootPane()).getChildren().get(0);

        // set the new text
        title.setNewText(msg);

        // align the border pane's contents to the center of the screen
        GridPane.setHalignment(title, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(title, javafx.geometry.VPos.CENTER);
    }

    @Override
    public void cleanup() {
        // nothing to clean up
    }
}
