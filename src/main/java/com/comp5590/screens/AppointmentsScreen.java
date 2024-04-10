package com.comp5590.screens;

import com.comp5590.components.LoginScreen.Title;
import com.comp5590.managers.ScreenManager;
import com.comp5590.security.managers.authentication.annotations.AuthRequired;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

@AuthRequired
public class AppointmentsScreen extends AbstractScreen {

    public AppointmentsScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // load css file
        this.addCss("/appointments.css");

        setRootPane(new BorderPane());
        ((BorderPane) getRootPane()).setTop(title()); // set title at the top
        ((BorderPane) getRootPane()).setCenter(center()); // set buttons in the center

        addBackAndHomeButtons(getRootPane()); //back and home buttons
    }

    private VBox title() {
        // title
        HBox titleBox = new Title("Appointments");
        titleBox.setId("title");
        VBox titleVBox = new VBox(titleBox);
        titleVBox.setAlignment(Pos.TOP_CENTER); // putting it in the center
        return titleVBox;
    }

    private HBox center() {
        // create buttons
        Button viewAppointmentsButton = new Button("View Appointments");
        viewAppointmentsButton.setId("viewAppointmentsButton");
        viewAppointmentsButton.setOnAction(event -> {
            // handle view appointments action
        });
        viewAppointmentsButton.getStyleClass().add("appointment-button");

        Button enterNewAppointmentButton = new Button("Enter New Appointment");
        enterNewAppointmentButton.setId("enterNewAppointmentButton");
        enterNewAppointmentButton.setOnAction(event -> {
            showScene(CreateBooking.class); // Link up with create booking screen
        });
        enterNewAppointmentButton.getStyleClass().add("appointment-button");

        Button changeAppointmentButton = new Button("Change Appointment");
        changeAppointmentButton.setId("changeAppointmentButton");
        changeAppointmentButton.setOnAction(event -> {
            // handle changing appointment action
        });
        changeAppointmentButton.getStyleClass().add("appointment-button");

        // add buttons to an HBox and style
        HBox buttonsBox = new HBox(viewAppointmentsButton, enterNewAppointmentButton, changeAppointmentButton);
        buttonsBox.setId("buttonsBox");
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setSpacing(20); // set spacing between buttons

        return buttonsBox;
    }

    @Override
    public void cleanup() {}
}
