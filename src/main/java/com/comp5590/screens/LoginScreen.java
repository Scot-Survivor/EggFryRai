package com.comp5590.screens;

import com.comp5590.managers.ScreenManager;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class LoginScreen extends AbstractScreen {

    public LoginScreen(ScreenManager screenManager){ // exception needed for hashing
        super(screenManager);
    }

    @Override
    public void setup() {
        // create the border pane object and add the login to the center
        BorderPane pane = new BorderPane();
        pane.setCenter(createLogin());
        pane.setTop(createTitle());
        setRootPane(pane);
        this.cssPath = "/login.css";
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
            getSceneManager().showScene(HomeScreen.class);
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
}
