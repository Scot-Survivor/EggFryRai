package com.comp5590.screens;

import com.comp5590.components.LoginScreen.Title;
import com.comp5590.managers.ScreenManager;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class ContactUsScreen extends AbstractScreen {

    // Define fields in class so submission can access them
    private TextField firstName;
    private TextField lastName;
    private TextField email;
    private TextArea message;
    private VBox formBox;

    public ContactUsScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // Load custom css
        this.addCss("/contactUs.css");

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
        submitButton.setId("submitButton");
        submitButton.setOnAction(this::submission);

        // Create text fields and labels
        Label firstNameLabel = new Label("First name");
        firstName = new TextField();
        Label lastNameLabel = new Label("Last name");
        lastName = new TextField();
        Label emailLabel = new Label("Email");
        email = new TextField();
        Label messageLabel = new Label("What is it you need assistance with?");
        message = new TextArea(); // Text area so input wraps

        Label bottomNote = new Label("Please note a response may take up to 5 working days.");

        // Set needed ids
        message.setId("messageTextBox");
        info.setId("infoLabel");
        bottomNote.setId("noteLabel");

        // Create form
        formBox =
        new VBox(
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
        formBox.setId("form");
        formBox.setAlignment(Pos.CENTER);

        // Add elements to VBox
        VBox center = new VBox(titleBox, info, formBox, bottomNote);
        center.setId("center");
        center.getStyleClass().add("custom-pane");
        center.setPrefSize(600, 400);
        // set max width to 250, and max height to fit content
        center.setMaxSize(600, 600);
        center.setAlignment(Pos.CENTER);
        return center;
    }

    private void submission(ActionEvent event) {
        // Boolean used to check if any values empty
        boolean filled = true;
        // Check each field, if empty highlight red, else remove highlighting
        if (firstName.getText().isEmpty()) {
            firstName.setStyle("-fx-effect: dropshadow(gaussian, rgba(255,0,0,0.5), 10, 0, 0, 0);");
            filled = false;
        } else {
            firstName.setStyle("-fx-effect: '';");
        }
        if (lastName.getText().isEmpty()) {
            lastName.setStyle("-fx-effect: dropshadow(gaussian, rgba(255,0,0,0.5), 10, 0, 0, 0);");
            filled = false;
        } else {
            lastName.setStyle("-fx-effect: '';");
        }
        if (email.getText().isEmpty()) {
            email.setStyle("-fx-effect: dropshadow(gaussian, rgba(255,0,0,0.5), 10, 0, 0, 0);");
            filled = false;
        } else {
            email.setStyle("-fx-effect: '';");
        }
        if (message.getText().isEmpty()) {
            message.setStyle("-fx-effect: dropshadow(gaussian, rgba(255,0,0,0.5), 10, 0, 0, 0);");
            filled = false;
        } else {
            message.setStyle("-fx-effect: '';");
        }

        // Check if filled or not, print result message
        if (!filled) {
            Label failed = new Label("You are missing details.");
            failed.setStyle("-fx-text-fill: red");
            formBox.getChildren().add(failed);
        } else {
            Label success = new Label("Your details have been sent.");
            success.setStyle("-fx-text-fill: green");
            formBox.getChildren().add(success);
            firstName.clear();
            lastName.clear();
            email.clear();
            message.clear();
        }
    }

    @Override
    public void cleanup() {}
}
