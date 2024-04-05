package com.comp5590.screens;

import com.comp5590.components.LoginScreen.Title;
import com.comp5590.managers.ScreenManager;
import java.util.ArrayList;
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
    private Label resultLabel;

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
        resultLabel = new Label(""); // Set as empty

        Label bottomNote = new Label("Please note a response may take up to 5 working days.");

        // Set needed ids
        firstName.setId("firstNameTextBox");
        lastName.setId("lastNameTextBox");
        email.setId("emailTextBox");
        message.setId("messageTextBox");
        resultLabel.setId("resultLabel");
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
            submitButton,
            resultLabel
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
        // Set result label to nothing
        resultLabel.setText("");
        // Boolean used to check if any values empty
        boolean filled = true;
        // Boolean used to check if email is valid
        boolean emailValid = true;
        // Check each field, if empty highlight red, else remove highlighting
        ArrayList<TextField> fields = new ArrayList<TextField>();
        fields.add(firstName);
        fields.add(lastName);

        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                field.setStyle("-fx-effect: dropshadow(gaussian, rgba(255,0,0,0.5), 10, 0, 0, 0);");
                filled = false;
            } else {
                field.setStyle("-fx-effect: '';");
            }
        }
        // Separate if statement for email to check if valid
        String emailText = email.getText();
        if (emailText.isEmpty()) {
            email.setStyle("-fx-effect: dropshadow(gaussian, rgba(255,0,0,0.5), 10, 0, 0, 0);");
            filled = false;
        } else if (!emailText.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            email.setStyle("-fx-effect: dropshadow(gaussian, rgba(255,0,0,0.5), 10, 0, 0, 0);");
            emailValid = false;
        } else {
            email.setStyle("-fx-effect: '';");
        }
        // Separate if statement for message as it is textArea
        if (message.getText().isEmpty()) {
            message.setStyle("-fx-effect: dropshadow(gaussian, rgba(255,0,0,0.5), 10, 0, 0, 0);");
            filled = false;
        } else {
            message.setStyle("-fx-effect: '';");
        }

        // Check if filled or not, print result message
        if (!filled) {
            resultLabel.setText("You are missing details.");
            resultLabel.setStyle("-fx-text-fill: red");
        } else if (!emailValid) {
            resultLabel.setText("Email is invalid.");
            resultLabel.setStyle("-fx-text-fill: red");
        } else {
            resultLabel.setText("Your details have been sent.");
            resultLabel.setStyle("-fx-text-fill: green");
            // Clear fields
            firstName.clear();
            lastName.clear();
            email.clear();
            message.clear();
        }
    }

    @Override
    public void cleanup() {}
}
