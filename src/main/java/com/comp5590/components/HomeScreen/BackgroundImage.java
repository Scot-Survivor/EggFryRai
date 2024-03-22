package com.comp5590.components.HomeScreen;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class BackgroundImage extends HBox {

    public BackgroundImage(String imgLink) {
        // make image then image view
        Image img = new Image(imgLink);
        ImageView imgView = new ImageView(img);

        // stretch to fit max X and Y of the screen
        imgView.fitWidthProperty().bind(this.widthProperty());
        imgView.fitHeightProperty().bind(this.heightProperty());
        imgView.setPreserveRatio(false); // don't preserve ratio

        // add image view to the hbox
        this.getChildren().add(imgView);

        // import and apply CSS
        this.getStylesheets().add("/home.css");
        this.getStyleClass().add("background-image");
    }
}
