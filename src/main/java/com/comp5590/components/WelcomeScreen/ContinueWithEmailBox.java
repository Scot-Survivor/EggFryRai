package com.comp5590.components.WelcomeScreen;

import io.github.palexdev.mfxcore.controls.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ContinueWithEmailBox extends HBox {

    public ContinueWithEmailBox() {
        // load css
        this.getStylesheets().add("/welcome.css");
        this.getStyleClass().add("continue-with-email-box");

        // create child components
        Text text = new Text("Continue with Email");

        Image image = new Image(getClass().getResourceAsStream("/at.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);

        // add child components to the HBox
        this.getChildren().addAll(text, imageView);
    }
}
