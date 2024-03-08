package com.comp5590.components.LoginScreen;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LoginField extends VBox {

    public LoginField(String info, TextField field, String placeholderText, String symbolPath) {
        // import css
        this.getStylesheets().add("/login.css");

        // make new hbox
        HBox hBox = new HBox();

        // create some text
        Text text = new Text(info);
        // increase font
        text.setStyle("-fx-font-size: 16px;");

        // adjust field width to max of the HBox, and other stylings
        field.prefWidthProperty().bind(this.widthProperty());
        field.setPromptText(placeholderText);
        field.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%); -fx-font-style: italic;");

        // create the image
        Image image = new Image(symbolPath);
        ImageView imageView = new ImageView(image);
        // fix icon to field's height
        imageView.setPreserveRatio(true);
        imageView.fitHeightProperty().bind(field.heightProperty());

        // add text to the VBox
        this.getChildren().add(text);

        // add the image and the field to the hbox (horizontally)
        hBox.getChildren().addAll(imageView, field);

        // add css to hBox
        hBox.getStyleClass().add("login-field");

        // set styling properties on the VBox (spacing, etc)
        this.setSpacing(5);

        // set styling properties on the HBox
        hBox.setSpacing(5);

        // add the HBox to the VBox (containing the field already)
        this.getChildren().add(hBox);
    }
}
