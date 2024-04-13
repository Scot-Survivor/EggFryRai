package com.comp5590.components.global;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class ScrollerBox extends VBox {

    public ScrollerBox(Node... nodes) {
        this.getStylesheets().add("/css/global.css");

        this.getChildren().addAll(nodes);
    }
}
