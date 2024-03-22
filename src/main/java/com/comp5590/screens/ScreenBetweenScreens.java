package com.comp5590.screens;

import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import com.comp5590.managers.SessionManager;
import javafx.scene.control.Label;
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
        Label label = new Label();
        label.getStyleClass().add("message");

        // align the border pane's contents to the center of the screen
        GridPane.setHalignment(label, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(label, javafx.geometry.VPos.CENTER);

        // add label to pane
        pane.getChildren().add(label);

        // set root pane
        setRootPane(pane);
    }

    public void runFunctionalityAfterSetup() {
        String msg = SessionManager.getInstance().getStateMessage();

        // if msg is null, redirect back to LoginScreen
        if (msg == null) {
            logger.info("No message found in SessionManager.getStateMessage(), redirecting to LoginScreen.");
            this.getApp().getScreenManager().showScene(LoginScreen.class);
            return;
        }

        // log the message
        logger.info(msg);

        // display the message on the Label inside the root pane
        kickstartMessageDisplayingMechanismProponent((Label) getRootPane().getChildren().get(0), msg);

        // after 5 seconds of forced waiting on main thread (nothing happens), redirect
        // to LoginScreen
        try {
            Thread.sleep(5000);
            this.getApp().getScreenManager().showScene(LoginScreen.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("Thread interrupted: " + e.getMessage());
        }
    }

    private void kickstartMessageDisplayingMechanismProponent(Label label, String msg) {
        label.setText(msg);
    }
}
