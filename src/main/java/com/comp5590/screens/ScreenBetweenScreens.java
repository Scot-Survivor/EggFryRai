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

        // otherwise, display the message
        GridPane pane = new GridPane();
        pane.getStyleClass().add("custom-pane");

        // create label with the msg
        Title title = new Title("Boilerplate");
        title.getStyleClass().add("message");

        // align the border pane's contents to the center of the screen
        GridPane.setHalignment(title, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(title, javafx.geometry.VPos.CENTER);

        // add title to pane
        pane.getChildren().add(title);

        // set root pane
        setRootPane(pane);
    }

    public void runFunctionalityAfterSetup(
        String msg,
        int waitTimeInSecs,
        Class<? extends AbstractScreen> nextScreenClass
    ) {
        // log the message
        logger.info(msg);

        // delete all children of pane
        GridPane pane = (GridPane) getRootPane();
        pane.getChildren().clear();

        // delete old title from pane
        getRootPane().getChildren().clear();

        // grab title from pane, and set the text to the message
        Title title = new Title(msg);
        title.getStyleClass().add("message");

        // add title to pane
        pane.getChildren().add(title);

        // after 5 seconds of forced waiting on main thread (nothing happens), redirect
        // to whatever screen is specified
        try {
            Thread.sleep(waitTimeInSecs * 1000);
            this.getApp().getScreenManager().showScene(nextScreenClass);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("Thread interrupted: " + e.getMessage());
        }
    }
}
