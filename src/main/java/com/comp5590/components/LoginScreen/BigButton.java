package com.comp5590.components.LoginScreen;

import javafx.scene.control.Button;

public class BigButton extends Button {

    public BigButton() {
        // import css
        this.getStylesheets().add("/css/login.css");

        // add the big-button class to the button
        this.getStyleClass().add("big-button");

        // put text on the button
        this.setText("Login");
    }
}
