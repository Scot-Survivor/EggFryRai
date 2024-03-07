package com.comp5590.components.LoginScreen;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LoginField {

    public LoginField(String info, String labelInput, String symbolPath) {
        // create some text
        Text text = new Text(info);
        // create the label
        Label label = new Label(labelInput);
        label.setId("labelInput");
        // create the image
        Image image = new Image(symbolPath);
        ImageView imageView = new ImageView(image);

        // add text to VBox
        VBox vBox = new VBox();
        vBox.getChildren().add(text);

        // add the image and the label to the HBox
        HBox hBox = new HBox();
        hBox.getChildren().addAll(imageView, label);

        // add the HBox to the VBox (containing the field already)
        vBox.getChildren().add(hBox);
    }
}
