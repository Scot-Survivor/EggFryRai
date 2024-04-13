package com.comp5590.screens;

import com.comp5590.components.LoginScreen.Title;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import com.comp5590.managers.SessionManager;
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
        this.addCss("/css/screenBetweenScreens.css");

        GridPane pane = new GridPane();
        pane.getStyleClass().add("custom-pane");

        Title title = new Title(SessionManager.getInstance().getStateMessage());
        title.getStyleClass().add("message");

        // add the title to the pane
        pane.add(title, 0, 0);

        GridPane.setHalignment(title, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(title, javafx.geometry.VPos.CENTER);

        // set root pane
        setRootPane(pane);
    }

    @Override
    public void cleanup() {
        // do nothing
    }
}
