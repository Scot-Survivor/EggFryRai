package com.comp5590.screens;

import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.core.Logger;

public class NewAppointmentScreen extends AbstractScreen {

    private final Logger logger = LoggerManager.getInstance().getLogger(NewAppointmentScreen.class);

    public NewAppointmentScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // Load custom css
        // this.addCss("/newAppointment.css");

        // attach default pane, but grab the reference to the gridpane (set as
        // center of borderpane) for further customization
        GridPane pane = this.attachDefaultPane();
        // attach header and nav bar
        this.attachHeaderAndNavBar("New Appointment");
    }

    @Override
    public void cleanup() {
        // nothing to clean up
    }
}
