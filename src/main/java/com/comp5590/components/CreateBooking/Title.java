package com.comp5590.components.CreateBooking;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Title {

    public Title(BorderPane rootPane) {
        // setting the title of the screen

        Text title = new Text("GP Alpha");
        title.setFill(javafx.scene.paint.Color.valueOf("#787efc"));
        title.setFont(Font.font("System Bold", 21));
        BorderPane.setMargin(title, new Insets(10.0));
        BorderPane.setAlignment(title, javafx.geometry.Pos.TOP_CENTER);

        rootPane.setTop(title);
    }
}
