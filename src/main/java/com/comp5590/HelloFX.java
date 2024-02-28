package com.comp5590;

import com.comp5590.database.Database;
import com.comp5590.secuirty.ArgonPasswordManager;
import com.comp5590.secuirty.PasswordManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloFX extends Application {
    private Database db;
    private PasswordManager pm;

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();

        db = new Database();
        pm = new ArgonPasswordManager();  // TODO: Write a factory setup for password managers.
    }

    public static void main(String[] args) {
        launch();
    }

    public Database getDatabase() {
        return db;
    }

    public PasswordManager getPm() {
        return pm;
    }
}
