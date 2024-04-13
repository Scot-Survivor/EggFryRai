package com.comp5590.components.RegisterScreen;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class BackToLoginBox extends HBox {

    public BackToLoginBox() {
        // import CSS
        this.getStylesheets().add("/css/register.css");

        // create new text
        Text text = new Text("Already have an account?");
        // add login-text style
        text.getStyleClass().add("login-text");

        // more text
        Text text2 = new Text("Log in here.");
        // add login-link style
        text2.getStyleClass().add("login-link");

        // set padding between the 2 texts in a horizontal fashion
        this.setSpacing(5);

        // add to hbox
        this.getChildren().addAll(text, text2);

        // set hbox to center
        this.setAlignment(javafx.geometry.Pos.CENTER);
    }
}
