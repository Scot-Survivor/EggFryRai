package com.comp5590.components.DoctorDetailsScreen;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class DoctorDisplayBox {

    public DoctorDisplayBox(VBox vbox) {
        VBox doctorDisplay = new VBox();
        doctorDisplay.setId("docDisplay");
        doctorDisplay.setAlignment(Pos.TOP_CENTER);

        vbox.getChildren().add(doctorDisplay);
    }
}
