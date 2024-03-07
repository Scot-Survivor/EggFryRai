package com.comp5590.components.LoginScreen;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Paragraph extends HBox {

    public Paragraph(String paragraph) {
        Text text = new Text(paragraph);
        text.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        text.setFill(Color.BLACK);
        getChildren().add(text);
    }
}
