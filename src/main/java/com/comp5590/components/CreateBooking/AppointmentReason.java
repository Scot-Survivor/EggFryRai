package com.comp5590.components.CreateBooking;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class AppointmentReason {

    public AppointmentReason(VBox vbox) {
        // Create the text field for the appt reason
        MFXTextField reasonTextField = new MFXTextField();
        reasonTextField.setPrefSize(360, 157);
        reasonTextField.setPromptText("Please specify the reason for your appointment");
        reasonTextField.setId("apptReasonTextField");
        VBox.setMargin(reasonTextField, new Insets(20.0));
        vbox.getChildren().add(reasonTextField);
    }
}
