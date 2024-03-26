package com.comp5590.components.HomeScreen;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class HugeImage extends HBox {

    public HugeImage(String imgLink) {
        // import CSS
        this.getStylesheets().add("/home.css");

        // make image then image view
        Image img = new Image(imgLink);
        ImageView imgView = new ImageView(img);

        // stretch to fit max X and Y of the screen
        imgView.setPreserveRatio(false); // don't preserve ratio

        // set hbox to take up all space
        this.setMaxWidth(Double.MAX_VALUE);

        // add image view to the hbox
        this.getChildren().add(imgView);
        this.getStyleClass().add("huge-image");
    }
}
