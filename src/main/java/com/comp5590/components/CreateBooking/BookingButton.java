package com.comp5590.components.CreateBooking;

import javafx.scene.control.Button;

public class BookingButton extends Button {

    public BookingButton() {
        // import css
        this.getStylesheets().add("/css/login.css");

        // add the big-button class to the button
        this.getStyleClass().add("big-button");

        // put text on the button
        this.setText("Book Appointment");
    }
}
