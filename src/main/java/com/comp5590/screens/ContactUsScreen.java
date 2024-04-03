package com.comp5590.screens;

import com.comp5590.components.LoginScreen.Title;
import com.comp5590.managers.ScreenManager;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class ContactUsScreen extends AbstractScreen {

    public ContactUsScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // Load custom css
        this.addCss("/docList.css");

        setRootPane(new BorderPane());
        ((BorderPane) getRootPane()).setCenter(center());

        // add navigation buttons
        addBackAndHomeButtons(getRootPane());
    }

    private VBox center() {
        // Create title
        HBox titleBox = new Title("Contact Us");
        titleBox.setId("title");

        Label info = new Label("Have an issue with our services? Let us know!");

        // Create button
        Button submitButton = new Button("Submit");
        submitButton.setId("switchButton");

        // Create text fields and labels
        Label firstNameLabel = new Label("First name");
        TextField firstName = new TextField();
        Label lastNameLabel = new Label("Last name");
        TextField lastName = new TextField();
        Label emailLabel = new Label("Email");
        TextField email = new TextField();
        Label messageLabel = new Label("What is it you need assistance with?");
        TextField message = new TextField();

        VBox formBox = new VBox(
            firstNameLabel,
            firstName,
            lastNameLabel,
            lastName,
            emailLabel,
            email,
            messageLabel,
            message,
            submitButton
        );
        formBox.setId("center");
        formBox.setAlignment(Pos.CENTER);

        // Add elements to VBox
        VBox center = new VBox(titleBox, info, formBox);
        center.setId("center");
        center.getStyleClass().add("custom-pane");
        center.setPrefSize(600, 400);
        // set max width to 250, and max height to fit content
        center.setMaxSize(600, 600);
        center.setAlignment(Pos.CENTER);
        return center;
    }

    @Override
    public void cleanup() {}
}
