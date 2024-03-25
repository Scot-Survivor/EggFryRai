package com.comp5590.utils;

import javafx.scene.layout.HBox;

public class QueryUtils {

    // return a class "javafx.scene.layout.HBox"
    public static Class<HBox> getHBoxClass() {
        return javafx.scene.layout.HBox.class;
    }

    // for Button
    public static Class<javafx.scene.control.Button> getButtonClass() {
        return javafx.scene.control.Button.class;
    }

    // for textFields
    public static Class<javafx.scene.control.TextField> getTextFieldClass() {
        return javafx.scene.control.TextField.class;
    }

    // for passwordFields
    public static Class<javafx.scene.control.PasswordField> getPasswordFieldClass() {
        return javafx.scene.control.PasswordField.class;
    }

    // for labels
    public static Class<javafx.scene.control.Label> getLabelClass() {
        return javafx.scene.control.Label.class;
    }
}
