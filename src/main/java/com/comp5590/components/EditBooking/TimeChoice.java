package com.comp5590.components.EditBooking;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class TimeChoice extends HBox {

    public TimeChoice(ChoiceBox<String> timeChoiceInput) {
        // import styling
        this.getStylesheets().add("/editBooking.css");

        // add styling
        this.getStyleClass().add("time-choice-box");

        // create label
        Label timeLabel = new Label("Please select a time");

        // create choice box
        timeChoiceInput.setId("time-choice");

        // add all times to the choice box, in 24 hour time (no AM/PM), with 30 minute
        // intervals
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j += 30) {
                String time = String.format("%02d:%02d", i, j);
                timeChoiceInput.getItems().add(time);
            }
        }

        // set the default value to the first time
        timeChoiceInput.setValue("09:00");

        // add label & choice box to the HBox
        this.getChildren().addAll(timeLabel, timeChoiceInput);
    }
}
