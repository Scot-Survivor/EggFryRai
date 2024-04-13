package com.comp5590.components.HomeScreen;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class NavBar extends HBox {

    // take infinite node arguments
    public NavBar(Button... buttons) {
        // load css
        this.getStylesheets().add("/css/home.css");

        // apply css to the navbar
        this.getStyleClass().add("navbar");

        // for each node
        for (Button button : buttons) {
            // apply css style to node
            button.getStyleClass().add("navbar-item");
            // add the node to the navbar
            this.getChildren().add(button);
        }
    }
}
