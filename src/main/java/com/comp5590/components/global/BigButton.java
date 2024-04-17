package com.comp5590.components.global;

import javafx.scene.control.Button;

public class BigButton extends Button {

    public BigButton(String txt) {
        // import css
        this.getStylesheets().add("/css/global.css");

        // add the big-button class to the button
        this.getStyleClass().add("big-button");

        // put text on the button
        this.setText(txt);
    }
}
