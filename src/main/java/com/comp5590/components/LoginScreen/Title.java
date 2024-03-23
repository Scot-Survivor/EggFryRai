package com.comp5590.components.LoginScreen;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Title extends HBox {

    public Title(String title) {
        Text text = new Text(title);
        text.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 40));
        // set fill to hex 575dfb
        text.setFill(Color.web("#575dfb"));
        getChildren().add(text);
    }

    public void setNewText(String title) {
        getChildren().clear();
        Text text = new Text(title);
        text.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 40));
        // set fill to hex 575dfb
        text.setFill(Color.web("#575dfb"));
        getChildren().add(text);
    }
}
