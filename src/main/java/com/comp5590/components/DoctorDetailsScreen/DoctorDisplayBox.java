package com.comp5590.components.DoctorDetailsScreen;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class DoctorDisplayBox extends VBox {

    /**
     * VBox to be blank until a doctor is selected
     */
    public DoctorDisplayBox() {
        this.setId("docDisplay");
        this.setAlignment(Pos.TOP_CENTER);
    }
}
