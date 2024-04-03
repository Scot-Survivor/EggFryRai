package com.comp5590.components.CreateBooking;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class WarningMessage {

    public WarningMessage(VBox vbox) {
        Text warningMessage = new Text("");
        warningMessage.setId("warningMessage");
        VBox.setMargin(warningMessage, new Insets(20.0));

        vbox.getChildren().add(warningMessage);
    }
}
