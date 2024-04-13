package com.comp5590.components.LoginScreen;

import javafx.scene.control.Button;

public class ForgotPasswordButton extends Button {

    public ForgotPasswordButton(String text) {
        // import css
        this.getStylesheets().add("/css/login.css");

        // set the text of the button
        this.setText(text);

        // set the style class of the button
        this.getStyleClass().add("forgot-password-button");
    }
}
