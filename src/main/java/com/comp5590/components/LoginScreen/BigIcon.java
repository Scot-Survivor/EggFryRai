package com.comp5590.components.LoginScreen;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class BigIcon extends VBox {

    public BigIcon(String path) {
        // create the image
        Image image = new Image(path);
        ImageView imageView = new ImageView(image);

        // set the size of the image
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);

        // add the image to the VBox
        this.getChildren().add(imageView);
    }
}
