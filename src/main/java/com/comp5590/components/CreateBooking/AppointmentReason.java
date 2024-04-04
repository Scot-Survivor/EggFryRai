package com.comp5590.components.CreateBooking;

import io.github.palexdev.materialfx.controls.MFXTextField;

public class AppointmentReason extends MFXTextField {

    public AppointmentReason() {
        // Create the text field for the appt reason
        this.setPrefSize(360, 157);
        this.setPromptText("Please specify the reason for your appointment");
        this.setId("apptReasonTextField");
    }
}
