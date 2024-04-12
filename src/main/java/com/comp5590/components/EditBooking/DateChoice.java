package com.comp5590.components.EditBooking;

import java.util.Date;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class DateChoice extends HBox {

    public DateChoice(DatePicker dateChoiceInput, Date date) {
        // import styling
        this.getStylesheets().add("/editBooking.css");

        // add styling
        this.getStyleClass().add("date-choice-box");

        // label
        Label dateLabel = new Label("Please select a date");

        // create a new date picker
        dateChoiceInput.setId("date-picker");

        // set the default value to the date
        dateChoiceInput.setValue(date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());

        // add label & date picker to the HBox
        this.getChildren().addAll(dateLabel, dateChoiceInput);
    }
}
