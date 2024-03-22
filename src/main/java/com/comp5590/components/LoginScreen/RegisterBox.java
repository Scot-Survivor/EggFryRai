package com.comp5590.components.LoginScreen;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class RegisterBox extends HBox {

    public RegisterBox() {
        // import CSS
        this.getStylesheets().add("/login.css");

        // create new text
        Text text = new Text("Don't have an account?");
        // add register-text style
        text.getStyleClass().add("register-text");

        // more text
        Text text2 = new Text("Register here.");
        // add register-link style
        text2.getStyleClass().add("register-link");

        // set padding between the 2 texts in a horizontal fashion
        this.setSpacing(5);

        // add to hbox
        this.getChildren().addAll(text, text2);

        // set hbox to center
        this.setAlignment(javafx.geometry.Pos.CENTER);
    }
}
