package com.comp5590.components.CreateBooking;

import java.time.LocalDate;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DatePickerBox {

    public DatePickerBox(VBox vbox) {
        // Create label for the date picker
        Label dateLabel = new Label("Select a date for your appointment");
        VBox.setMargin(dateLabel, new Insets(10.0));

        // Date picker object
        javafx.scene.control.DatePicker datePicker = new javafx.scene.control.DatePicker();

        // set the minimum time to be in the future
        datePicker.setDayCellFactory(picker ->
            new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.compareTo(LocalDate.now()) < 0);
                }
            }
        );
        datePicker.setId("datePicker");

        // Now pick the time
        Label timeLabel = new Label("Please select a time");
        VBox.setMargin(timeLabel, new Insets(10.0));
        ChoiceBox<String> timeChoiceBox = new ChoiceBox<>();
        timeChoiceBox.setPrefWidth(150.0);
        timeChoiceBox.setId("time"); // give the choice box an id
        addTimes(timeChoiceBox);

        // Create the VBox to store these items and then return it
        VBox dateBox = new VBox(dateLabel, datePicker, timeLabel, timeChoiceBox);
        dateBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        vbox.getChildren().add(dateBox);
    }

    /**
     * This is terrible. Please ignore the way I am doing this
     * @param timeChoiceBox
     */
    private void addTimes(ChoiceBox<String> timeChoiceBox) {
        timeChoiceBox
            .getItems()
            .addAll(
                "08:00",
                "08:30",
                "09:00",
                "09:30",
                "10:00",
                "10:30",
                "11:00",
                "11:30",
                "12:00",
                "12:30",
                "13:00",
                "13:30",
                "14:00",
                "14:30",
                "15:00",
                "15:30",
                "16:00"
            );
    }
}
