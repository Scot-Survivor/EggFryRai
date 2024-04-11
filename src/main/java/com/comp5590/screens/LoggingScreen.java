package com.comp5590.screens;

import com.comp5590.components.LoginScreen.Title;
import com.comp5590.configuration.AppConfig;
import com.comp5590.managers.ScreenManager;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
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

        HBox logBox = new HBox();
        logBox.setId("logBox");
        logBox.setAlignment(Pos.TOP_CENTER);

        ListView<String> logHistory = new ListView<>();
        logHistory.setId("logHistory");

        logHistory.getItems().addAll(logs);

        // Set preferred width to 40% larger than the longest width of string
        logHistory.setPrefWidth(
            (logHistory.getItems().stream().mapToInt(String::length).max().orElse((int) getRootPane().getWidth())) * 1.4
        );

        logBox.getChildren().add(logHistory);

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
