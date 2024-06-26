package com.comp5590.components.HomeScreen;

import com.comp5590.components.LoginScreen.Title;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class HeaderBar extends HBox {

    public HeaderBar(HBox left, String leftText, String leftIconURL, HBox right, String firstName, String title) {
        // * LEFT
        // HBox containing the icon and "Welcome, <name>" button (passed into the
        // constructor, to apply event listener externally)

        // text field for the text to put in left box of header
        Text leftTextText = new Text(leftText);
        // icon variable for storing the image
        Image iconUser = new Image(leftIconURL);
        ImageView iconViewUser = new ImageView(iconUser);
        iconViewUser.preserveRatioProperty().set(true);
        iconViewUser.fitHeightProperty().bind(this.prefHeightProperty());

        // add the icon and text to the left HBox
        left.getChildren().addAll(iconViewUser, leftTextText);

        // * MIDDLE
        // HBox to contain the vbox
        HBox middle = new HBox();
        // VBox containing the GP name
        VBox middleChildVBox = new VBox();
        // Text containing GP name
        Title gpNameText = new Title(title);

        // add the GP name to the middle VBox, then add the VBox to the middle HBox
        middleChildVBox.getChildren().add(gpNameText);
        middle.getChildren().add(middleChildVBox);

        // style vbox to be vertically aligned to the center of the hbox
        middleChildVBox.setAlignment(Pos.CENTER);

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

        // set the middle vbox to be aligned vertically & horizontally in the center
        HBox.setHgrow(left, Priority.NEVER);
        HBox.setHgrow(middle, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.NEVER);

        // add the HBoxes to the header bar
        this.getChildren().addAll(left, middle, right);

        // set the header bar to take up the entire width of the screen
        this.setMaxWidth(Double.MAX_VALUE);
        // set the header bar to be aligned in the center
        this.setAlignment(Pos.CENTER);

        // import & apply css
        this.getStylesheets().add("/css/home.css");
        this.getStyleClass().add("header-bar");
        left.getStyleClass().add("header-bar-left");
        middle.getStyleClass().add("header-bar-middle");
        right.getStyleClass().add("header-bar-right");
    }
}
