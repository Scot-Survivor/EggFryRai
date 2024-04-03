package com.comp5590.screens;

import com.comp5590.components.LoginScreen.Title;
import com.comp5590.managers.ScreenManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AppointmentsScreen extends AbstractScreen {

    public AppointmentsScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // Load custom CSS
        this.addCss("/appointments.css");

        setRootPane(new BorderPane());
        ((BorderPane) getRootPane()).setCenter(center());

        // Add navigation buttons
        addBackAndHomeButtons(getRootPane());
    }

    private VBox center() {
        // Create title
        HBox titleBox = new Title("Appointments");
        titleBox.setId("title");

        // Create button
        Button someButton = new Button("Some Button");
        someButton.setId("someButton");
        someButton.setOnAction(event -> {
            // Handle button action
        });

        // Add elements to VBox
        VBox center = new VBox(titleBox, someButton);
        center.setId("center");
        center.getStyleClass().add("custom-pane");
        center.setPrefSize(600, 400);
        center.setAlignment(Pos.CENTER);
        return center;
    }

    @Override
    public void cleanup() {
        // Cleanup if needed
    }
}
