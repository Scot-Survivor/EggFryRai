package com.comp5590.components.CreateBooking;

import com.comp5590.managers.LoggerManager;
import java.time.LocalDate;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.apache.logging.log4j.core.Logger;

public class DatePickerBox extends VBox {

    @Getter
    private final ChoiceBox<String> choiceBox;

    @Getter
    private final DatePicker datePicker;

    private final Logger logger;

    public DatePickerBox() {
        logger = LoggerManager.getInstance().getLogger(DatePickerBox.class);
        // Create label for the date picker
        Label dateLabel = new Label("Select a date for your appointment");
        VBox.setMargin(dateLabel, new Insets(10.0));

        // Date picker object
        datePicker = new javafx.scene.control.DatePicker();

        // set the minimum time to be in the future
        datePicker.setDayCellFactory(picker ->
            new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.isBefore(LocalDate.now()));
                }
            }
        );
        datePicker.setId("datePicker");

        // Now pick the time
        Label timeLabel = new Label("Please select a time");
        VBox.setMargin(timeLabel, new Insets(10.0));
        choiceBox = new ChoiceBox<>();
        choiceBox.setPrefWidth(150.0);
        choiceBox.setId("time"); // give the choice box an id
        addTimes();

        // Create the VBox to store these items and then return it
        VBox dateBox = new VBox(dateLabel, datePicker, timeLabel, choiceBox);
        dateBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        this.getChildren().add(dateBox);
    }

    /**
     * This is terrible. Please ignore the way I am doing this
     */
    private void addTimes() {
        choiceBox
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
