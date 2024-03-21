package com.comp5590.screens;

/**
 * Temporary will act as a home
 */

import com.comp5590.managers.ScreenManager;
import com.comp5590.security.managers.authentication.annotations.AuthRequired;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

@AuthRequired
public class HomeScreen extends AbstractScreen {

    public HomeScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        BorderPane rootPane = new BorderPane();
        setRootPane(rootPane);
        // We know that root is always going to be a BorderPane so its safe to cast without checking.
        rootPane.setCenter(center());
        rootPane.setLeft(left());
    }

    private VBox center() {
        Text welcome = new Text("Welcome");
        VBox central = new VBox(welcome);
        return central;
    }

    /**
     * Create a temporary left hand button to move to a given screen
     * @return A VBox containing that button
     */
    private VBox left() {
        Button makeBooking = new Button();
        makeBooking.setText("Bookings");
        makeBooking.setOnAction(this::moveBookings);

        VBox left = new VBox(makeBooking);
        return left;
    }

    /**
     * Move across to the bookings screen
     */
    private void moveBookings(ActionEvent event) {
        getApp().getScreenManager().showScene(CreateBooking.class);
    }
}
