package com.comp5590.components.global;

import com.comp5590.database.entities.Notification;
import com.comp5590.database.entities.User;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class NotificationCard extends VBox {

    public NotificationCard(Notification notification, Button readButton, Button deleteButton) {
        // setup styles
        this.getStylesheets().add("/css/global.css");

        // add styles
        this.getStyleClass().add("notification-card");

        // grab fields from notification
        User user = notification.getUser();
        String message = notification.getMessage();
        boolean read = notification.isRead();

        // make fields for Notification
        Text timestampText = new Text("Timestamp: ");
        Text userText = new Text("For user: ");
        Text messageText = new Text("Message: ");
        Text readText = new Text("Read: ");

        // make fields for Notification
        Text timestampValue = new Text(notification.getTimestamp().toString());
        Text userValue = new Text(user.getFirstName() + " " + user.getSurName());
        Text messageValue = new Text(message);
        Text readValue = new Text(read ? "Yes" : "No");

        // give styles to the values
        timestampValue.getStyleClass().add("notification-card-value");
        userValue.getStyleClass().add("notification-card-value");
        messageValue.getStyleClass().add("notification-card-value");
        readValue.getStyleClass().add("notification-card-value");

        // put all of above into their own HBox
        HBox timestampBox = new HBox(timestampText, timestampValue);
        HBox userBox = new HBox(userText, userValue);
        HBox messageBox = new HBox(messageText, messageValue);
        HBox readBox = new HBox(readText, readValue);

        // apply styling to the hbox rows
        userBox.getStyleClass().add("notification-card-row");

        // add buttons to their own HBox
        HBox buttonBox = new HBox(readButton, deleteButton);
        // apply styles to the button box
        buttonBox.getStyleClass().add("notification-card-button-box");

        // apply styles to the buttons
        readButton.getStyleClass().add("notification-card-button");
        deleteButton.getStyleClass().add("notification-card-button");

        // add all of the above to the VBox
        this.getChildren().addAll(timestampBox, userBox, messageBox, readBox, buttonBox);
    }
}
