package com.comp5590.components.EditBooking;

import com.comp5590.components.LoginScreen.BigButton;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class EditBookingCard extends VBox {

    public EditBookingCard(
        DoctorChoice doctorChoice,
        RoomChoice roomChoice,
        DateChoice dateChoice,
        TimeChoice timeChoice,
        BigButton confirmBtn,
        Label erroLabel
    ) {
        // styling
        this.getStylesheets().add("/editBooking.css");

        // add styling
        this.getStyleClass().add("edit-booking-card");

        // add everything to the card
        this.getChildren().addAll(doctorChoice, roomChoice, dateChoice, timeChoice, confirmBtn, erroLabel);
    }
}
