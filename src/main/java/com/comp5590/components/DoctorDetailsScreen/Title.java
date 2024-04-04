package com.comp5590.components.DoctorDetailsScreen;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Title extends Text {

    public Title() {
        // setting the title of the screen

        this.setText("GP Alpha");
        this.setFill(javafx.scene.paint.Color.valueOf("#787efc"));
        this.setFont(Font.font("System Bold", 21));
        BorderPane.setMargin(this, new Insets(10.0));
        BorderPane.setAlignment(this, javafx.geometry.Pos.TOP_CENTER);
    }
}
