package com.comp5590.screens;

import com.comp5590.managers.ScreenManager;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

//TODO: Add @AuthRequired
public class CreateBooking extends AbstractScreen {

    public CreateBooking(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // set the main pane of the screen
        BorderPane rootPane = new BorderPane();
        setRootPane(rootPane);

        // set center to be some regular text
        rootPane.setTop(setTitle());
        rootPane.setCenter(createCenter());
    }

    /**
     * Create the title item of the bookings screen
     * @return
     */
    private Text setTitle() {
        Text title = new Text("GP Alpha");
        title.setFill(javafx.scene.paint.Color.valueOf("#787efc"));
        title.setStrokeWidth(0.0);
        title.setFont(Font.font("System Bold", 21));
        BorderPane.setMargin(title, new Insets(10.0));
        BorderPane.setAlignment(title, javafx.geometry.Pos.TOP_CENTER);

        return title;
    }

    /**
     * Create the center items of the bookings screen
     * @return A BorderPane containing these items
     */
    private BorderPane createCenter() {
        BorderPane centerPane = new BorderPane();
        centerPane.setPrefSize(200, 200);
        BorderPane.setAlignment(centerPane, javafx.geometry.Pos.CENTER);

        Text headerText = new Text("Book Appointment");
        headerText.setStrokeWidth(0.0);
        BorderPane.setAlignment(headerText, javafx.geometry.Pos.CENTER);
        centerPane.setTop(headerText);

        VBox vBox = new VBox();
        vBox.setPrefSize(100, 200);
        vBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        BorderPane.setAlignment(vBox, javafx.geometry.Pos.CENTER);

        MFXTextField reasonTextField = new MFXTextField();
        reasonTextField.setPrefSize(360, 157);
        reasonTextField.setPromptText("Please specify the reason for your appointment");
        VBox.setMargin(reasonTextField, new Insets(20.0));

        Label dateLabel = new Label("Select a date for your appointment");
        VBox.setMargin(dateLabel, new Insets(10.0));
        DatePicker datePicker = new DatePicker();

        Label doctorLabel = new Label("Please select a doctor");
        VBox.setMargin(doctorLabel, new Insets(10.0));
        ChoiceBox<String> doctorChoiceBox = new ChoiceBox<>();
        doctorChoiceBox.setPrefWidth(150.0);

        vBox.getChildren().addAll(reasonTextField, dateLabel, datePicker, doctorLabel, doctorChoiceBox);
        centerPane.setCenter(vBox);

        return centerPane;
    }
}
