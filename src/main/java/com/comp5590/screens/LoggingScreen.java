package com.comp5590.screens;

import com.comp5590.configuration.AppConfig;
import com.comp5590.managers.ScreenManager;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class LoggingScreen extends AbstractScreen {

    private final List<String> logs;
    private final ListView<String> logHistory = new ListView<>();

    public LoggingScreen(ScreenManager screenManager) {
        super(screenManager);
        logs = new ArrayList<>();
    }

    @Override
    public void setup() {
        this.addCss("/css/logging.css");

        // default pane (with gradpane returned for further customization)
        GridPane pane = attachDefaultPane();

        // attach header and navbar
        attachHeaderAndNavBar("Logs");

        ((BorderPane) getRootPane()).setCenter(center());
    }

    @Override
    public void cleanup() {}

    private VBox center() {
        HBox logBox = new HBox();
        logBox.setId("logBox");
        logBox.setAlignment(Pos.TOP_CENTER);

        logHistory.setId("logHistory");

        logHistory.setCellFactory(param ->
            new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item);
                        // set colour based on log level
                        // DEBUG : GREEN INFO : BLACK WARN : YELLOW ERROR : RED
                        // ALL SHOULD BE BOLD
                        String styleString = "-fx-font-weight: bold;";
                        if (item.contains("DEBUG")) {
                            setStyle(styleString + " -fx-text-fill: green;");
                        } else if (item.contains("INFO")) {
                            setStyle(styleString + " -fx-text-fill: black;");
                        } else if (item.contains("WARN")) {
                            setStyle(styleString + " -fx-text-fill: yellow;");
                        } else if (item.contains("ERROR") || item.contains("FATAL")) {
                            setStyle(styleString + " -fx-text-fill: red;");
                        } else if (item.contains("TRACE")) {
                            setStyle(styleString + " -fx-text-fill: blue;");
                        }
                    }
                }
            }
        );

        logHistory.getItems().addAll(logs);

        // Set preferred width to 40% larger than the longest width of string
        logHistory.setPrefWidth(
            (logHistory.getItems().stream().mapToInt(String::length).max().orElse((int) getRootPane().getWidth())) * 1.4
        );

        logBox.getChildren().add(logHistory);

        VBox centerBox = new VBox(logBox);
        centerBox.setAlignment(Pos.TOP_CENTER);
        return centerBox;
    }

    /**
     * Add a log to the screen
     *
     * @param log the log to add
     */
    public void addLog(String log) {
        if (logs.size() > AppConfig.MAX_LOG_RESULTS) {
            logs.remove(0);
        }
        logs.add(log);
    }
}
