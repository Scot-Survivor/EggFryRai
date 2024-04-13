package com.comp5590.screens;

import com.comp5590.components.global.ScrollerBox;
import com.comp5590.configuration.AppConfig;
import com.comp5590.managers.ScreenManager;
import com.comp5590.security.authentication.annotations.AuthRequired;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
@AuthRequired
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

        // generate log box
        HBox center = center();

        // generate scrollable log box
        ScrollPane scrollPane = new ScrollPane();

        ScrollerBox scrollerBox = new ScrollerBox();
        scrollerBox.getChildren().add(center);

        scrollPane.setContent(scrollerBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        center.setAlignment(Pos.CENTER);
        center.prefWidthProperty().bind(scrollPane.widthProperty());
        center.prefHeightProperty().bind(scrollPane.heightProperty());

        // set the center of the borderpane to the scrollpane
        ((BorderPane) getRootPane()).setCenter(scrollPane);
    }

    @Override
    public void cleanup() {}

    private HBox center() {
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

                        // also set background colour for each using a different colour, ensuring good
                        // contrast against text fill colour

                        String styleString = "-fx-font-weight: bold;";
                        if (item.contains("DEBUG")) {
                            setStyle(styleString + " -fx-text-fill: white; -fx-background-color: black;");
                        } else if (item.contains("INFO")) {
                            setStyle(styleString + " -fx-text-fill: white; -fx-background-color: blue;");
                        } else if (item.contains("WARN")) {
                            setStyle(styleString + " -fx-text-fill: black; -fx-background-color: yellow;");
                        } else if (item.contains("ERROR") || item.contains("FATAL")) {
                            setStyle(styleString + " -fx-text-fill: black; -fx-background-color: red;");
                        } else if (item.contains("TRACE")) {
                            setStyle(styleString + " -fx-text-fill: white; -fx-background-color: purple;");
                        }

                        // Add hover effects
                        setOnMouseEntered(event -> setStyle(getStyle() + "-fx-background-color: #CCCCCC;"));
                        setOnMouseExited(event -> setStyle(getStyle().replace("-fx-background-color: #CCCCCC;", "")));

                        setBorder(
                            new Border(
                                new BorderStroke(
                                    Color.DARKSLATEGREY,
                                    BorderStrokeStyle.SOLID,
                                    CornerRadii.EMPTY,
                                    new BorderWidths(1)
                                )
                            )
                        );

                        setPadding(new Insets(1, 2, 1, 2));
                    }
                }
            }
        );

        logHistory.getItems().addAll(logs);

        // sort logs in reverse order (so newest logs are at the top)
        logHistory.getItems().sort(Comparator.reverseOrder());

        // Set preferred width to 40% larger than the longest width of string
        logHistory.setPrefWidth(
            (logHistory.getItems().stream().mapToInt(String::length).max().orElse((int) getRootPane().getWidth())) * 1.4
        );

        logBox.getChildren().add(logHistory);

        return logBox;
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
        // Hacky to prevent duplication of logs
        if (!logs.isEmpty() && logs.get(logs.size() - 1).equals(log)) {
            return;
        }
        logs.add(log);
    }
}
