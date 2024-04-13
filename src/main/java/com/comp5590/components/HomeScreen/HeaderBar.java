package com.comp5590.components.HomeScreen;

import com.comp5590.components.LoginScreen.Title;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class HeaderBar extends HBox {

    public HeaderBar(HBox left, HBox right, String firstName, String title) {
        // * LEFT
        // HBox containing the icon and "Welcome, <name>" button (passed into the
        // constructor, to apply event listener externally)

        // text field for the Welcome, <name> text
        Text welcomeText = new Text(String.format("Welcome, %s", firstName));
        // icon variable for storing the user image
        Image iconUser = new Image("/images/user.png");
        ImageView iconViewUser = new ImageView(iconUser);
        iconViewUser.preserveRatioProperty().set(true);
        iconViewUser.fitHeightProperty().bind(this.prefHeightProperty());

        // add the icon and text to the left HBox
        left.getChildren().addAll(iconViewUser, welcomeText);

        // * MIDDLE
        // HBox containing the GP name
        HBox middle = new HBox();

        // Text containing GP name
        Title gpNameText = new Title(title);

        // add the GP name to the middle HBox
        middle.getChildren().add(gpNameText);

        // * RIGHT
        // HBox containing the logout button (passed into the constructor, to apply
        // event listener externally)

        // text field for the logout button
        Text logoutText = new Text("Logout");

        // logout button
        Image iconLogout = new Image("/images/logout.png");
        ImageView iconViewLogout = new ImageView(iconLogout);
        iconViewLogout.preserveRatioProperty().set(true);
        iconViewLogout.fitHeightProperty().bind(this.prefHeightProperty());

        // add the icon and text to the right HBox
        right.getChildren().addAll(logoutText, iconViewLogout);

        // add the HBoxes to the header bar
        this.getChildren().addAll(left, middle, right);

        // ensure left is always far left of screen, right is always far right, and
        // midle is middle
        HBox.setHgrow(left, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(middle, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(right, javafx.scene.layout.Priority.ALWAYS);

        // set the header bar to take up the entire width of the screen
        this.setMaxWidth(Double.MAX_VALUE);

        // import & apply css
        this.getStylesheets().add("/css/home.css");
        this.getStyleClass().add("header-bar");
        left.getStyleClass().add("header-bar-left");
        middle.getStyleClass().add("header-bar-middle");
        right.getStyleClass().add("header-bar-right");
    }
}
