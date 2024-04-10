package com.comp5590.screens;

import com.comp5590.components.LoginScreen.Title;
import com.comp5590.configuration.AppConfig;
import com.comp5590.managers.ScreenManager;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class LoggingScreen extends AbstractScreen {

    private final List<String> logs;

    public LoggingScreen(ScreenManager screenManager) {
        super(screenManager);
        logs = new ArrayList<>();
    }

    @Override
    public void setup() {
        this.addCss("/css/logging.css");

        setRootPane(new BorderPane());
        ((BorderPane) getRootPane()).setCenter(center());

        // add navigation buttons
        addBackAndHomeButtons(getRootPane());
    }

    @Override
    public void cleanup() {}

    private VBox center() {
        // Create title
        HBox titleBox = new Title("Recent Logs");
        titleBox.setId("title");
        titleBox.setAlignment(Pos.TOP_CENTER);

        VBox logBox = new VBox();
        logBox.setId("logBox");
        logs.forEach(log -> {
            Label logLabel = new Label(log);
            logLabel.setWrapText(true);
            logBox.getChildren().add(logLabel);
        });
        logBox.setAlignment(Pos.TOP_CENTER);

        VBox centerBox = new VBox(titleBox, logBox);
        centerBox.setAlignment(Pos.TOP_CENTER);
        return centerBox;
    }

    /**
     * Add a log to the screen
     * @param log the log to add
     */
    public void addLog(String log) {
        if (logs.size() > AppConfig.MAX_LOG_RESULTS) {
            logs.remove(0);
        }
        logs.add(log);
    }
}
