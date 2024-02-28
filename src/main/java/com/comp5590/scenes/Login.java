package com.comp5590.scenes;

import com.comp5590.SceneManager;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class Login {

    private SceneManager main;
    private BorderPane mainPane;

    public Login(SceneManager main){ // exception needed for hashing
        this.main = main;

        // create the border pane object and add the login to the center
        mainPane = new BorderPane();
        mainPane.setCenter(createLogin());
        mainPane.setTop(createTitle());
    }

    /**
     * A function that creates a button to send us to the POS screen
     * @return The button
     */
    private VBox createLogin(){
        Label emailText = new Label("Email:");
        TextField email = new TextField();
        Label passwordText = new Label("Password:");
        PasswordField password = new PasswordField();

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            main.showHomeScreen();
        });

        VBox vbox = new VBox(emailText, email, passwordText, password, loginButton);
        return vbox;
    }

    private HBox createTitle(){
        Text text = new Text("Please Login");
        text.getStyleClass().add("title-text");
        HBox titleBox = new HBox(text);
        titleBox.getStyleClass().add("title");
        return titleBox;
    }

    /**
     * Gets the root node of the given interface
     * @return BorderPane which is the root node
     */
    public BorderPane getRoot(){
        return mainPane;
    }
}
