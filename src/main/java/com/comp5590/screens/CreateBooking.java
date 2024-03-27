package com.comp5590.screens;

// TODO: Make it consistent with the design schema

import com.comp5590.database.entities.Booking;
import com.comp5590.database.entities.Room;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.managers.ScreenManager;
import com.comp5590.managers.SessionManager;
import com.comp5590.security.managers.authentication.annotations.AuthRequired;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

@AuthRequired
public class CreateBooking extends AbstractScreen {

    private User currentUser;
    private HashMap<String, Integer> doctorMap;
    private HashMap<String, Integer> roomMap;

    public CreateBooking(ScreenManager screenManager) {
        super(screenManager);
        doctorMap = new HashMap<>();
        roomMap = new HashMap<>();
    }

    @Override
    public void setup() {
        // get the current user for later use
        // currentUser = App.getInstance().getCurrentUser();

        // set the main pane of the screen
        BorderPane rootPane = new BorderPane();
        setRootPane(rootPane);

        // set center to be some regular text
        rootPane.setTop(setTitle());
        rootPane.setCenter(createCenter());
        rootPane.setLeft(createLeft());
    }

    /**
     * Create the title item of the bookings screen
     * @return A Text Object that will function as the title
     */
    private Text setTitle() {
        // setting the title of the screen

        Text title = new Text("GP Alpha");
        title.setFill(javafx.scene.paint.Color.valueOf("#787efc"));
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
        // Create the border pane that will store the center items
        BorderPane centerPane = new BorderPane();
        centerPane.setPrefSize(200, 200);
        BorderPane.setAlignment(centerPane, javafx.geometry.Pos.CENTER);

        // Set the head of the border pane to be the title of the specific page
        Text headerText = new Text("Book Appointment");
        headerText.setStrokeWidth(0.0);
        BorderPane.setAlignment(headerText, javafx.geometry.Pos.CENTER);
        centerPane.setTop(headerText);

        // Create the Vbox that will store all the center items of the sub VBox
        VBox vBox = new VBox();
        vBox.setPrefSize(100, 200);
        vBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        BorderPane.setAlignment(vBox, javafx.geometry.Pos.CENTER);

        // Add all the sub items to the VBox
        vBox
            .getChildren()
            .addAll(
                createApptReasonField(),
                createWholeDatePicker(),
                createWholeDoctorChoice(),
                createWholeRoomChoice(),
                createBookingButton()
            );
        centerPane.setCenter(vBox);

        // return the central pane of the page
        return centerPane;
    }

    /**
     * Create the left side items of the bookings screen
     * @return A VBox just containing the buttons
     */
    private VBox createLeft() {
        // Create the button and add functionality
        Button homeButton = new Button("Home");
        homeButton.setOnAction(e -> {
            showScene(HomeScreen.class);
        });

        // Create and return the VBox to hold these
        VBox leftSide = new VBox(homeButton);
        return leftSide;
    }

    /**
     * Create the text field for the appointment reason
     * @return A MFXTextField object
     */
    private MFXTextField createApptReasonField() {
        // Create the text field for the appt reason
        MFXTextField reasonTextField = new MFXTextField();
        reasonTextField.setPrefSize(360, 157);
        reasonTextField.setPromptText("Please specify the reason for your appointment");
        reasonTextField.setId("apptReasonTextField");
        VBox.setMargin(reasonTextField, new Insets(20.0));
        return reasonTextField;
    }

    /**
     * Create the date picker for the appointment
     * @return A VBox containing a Label and a DatePicker
     */
    private VBox createWholeDatePicker() {
        // Create label for the date picker
        Label dateLabel = new Label("Select a date for your appointment");
        VBox.setMargin(dateLabel, new Insets(10.0));

        // Date picker object
        DatePicker datePicker = new DatePicker();

        // set the minimum time to be in the future
        datePicker.setDayCellFactory(picker ->
            new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.compareTo(LocalDate.now()) < 0);
                }
            }
        );
        datePicker.setId("datePicker");

        // Create the VBox to store these items and then return it
        VBox dateBox = new VBox(dateLabel, datePicker);
        dateBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        return dateBox;
    }

    /**
     * Create the label and choice box for the doctor choice
     * @return A VBox containing a label and a Choice Box
     */
    private VBox createWholeDoctorChoice() {
        // Create the label for the doctor choice box
        Label doctorLabel = new Label("Please select a doctor");
        VBox.setMargin(doctorLabel, new Insets(10.0));
        ChoiceBox<String> doctorChoiceBox = new ChoiceBox<>();
        doctorChoiceBox.setPrefWidth(150.0);
        doctorChoiceBox.setId("doctorChoiceBox"); // give the choice box an id

        // Get all the information on the doctor class
        DatabaseManager db = getDatabaseManager();

        // Grab a list of doctors
        List<?> docList = db.query("FROM User WHERE role = 'DOCTOR'");

        // Attempt to add all doctors to the drop down. If fails then just print message and don't display and doctors
        try {
            for (Object doc : docList) {
                int docId = ((User) doc).getId();
                String docName = ((User) doc).getFirstName() + " " + ((User) doc).getSurName();
                doctorChoiceBox.getItems().add(docName);
                doctorMap.put(docName, docId);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // create the VBox to store these items and then return it
        VBox doctorBox = new VBox(doctorLabel, doctorChoiceBox);
        doctorBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        return doctorBox;
    }

    /**
     * Create the doctor choice drop down menu
     * @return A VBox containing the required fields
     */
    private VBox createWholeRoomChoice() {
        // Create the label for the doctor choice box
        Label roomLabel = new Label("Please select a room");
        VBox.setMargin(roomLabel, new Insets(10.0));
        ChoiceBox<String> roomChoiceBox = new ChoiceBox<>();
        roomChoiceBox.setPrefWidth(150.0);
        roomChoiceBox.setId("roomChoiceBox"); // give the choice box an id

        // Get all the information on the doctor class
        DatabaseManager db = getDatabaseManager();

        // Grab a list of doctors
        List<?> roomList = db.query("FROM Room");

        // Attempt to add all doctors to the drop down. If fails then just print message and don't display and doctors
        try {
            for (Object room : roomList) {
                int roomId = ((Room) room).getRoomId();
                String roomName = ((Room) room).getRoomNumber();
                roomChoiceBox.getItems().add(roomName);
                roomMap.put(roomName, roomId);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // create the VBox to store these items and then return it
        VBox doctorBox = new VBox(roomLabel, roomChoiceBox);
        doctorBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        return doctorBox;
    }

    /**
     * Create the booking button
     * @return A Button object that will allow the user to book an appointment
     */
    private Button createBookingButton() {
        // Create booking button and add some padding
        Button book = new Button("Book Appointment");
        VBox.setMargin(book, new Insets(10.0));

        // Add some functionality to the button
        book.setOnAction(this::book);

        return book;
    }

    /**
     * The functionality to allow the booking of an event
     * @param event
     */
    private void book(ActionEvent event) {
        // open a db session
        DatabaseManager db = getDatabaseManager();
        Pane root = getRootPane(); // get the root pane

        // Grab the text field
        MFXTextField textField = (MFXTextField) root.lookup("#apptReasonTextField");

        // get the id of the doctor
        ChoiceBox<String> docSelection = (ChoiceBox<String>) root.lookup("#doctorChoiceBox");
        int docId = doctorMap.get(docSelection.getValue());
        List<?> doctorResult = db.query("FROM User WHERE id = " + docId);
        User doctor = (User) doctorResult.get(0);

        // get the apt reason
        String apptReason = ((TextField) root.lookup("#apptReasonTextField")).getText();

        // Get the appt date
        LocalDate datePicker = ((DatePicker) root.lookup("#datePicker")).getValue();
        Date apptDate = java.sql.Date.valueOf(datePicker);

        // get the current user
        User currentUser = SessionManager.getInstance().getCurrentUser();

        // get the users current room choice
        ChoiceBox<String> roomChoiceBox = (ChoiceBox<String>) root.lookup("#roomChoiceBox");
        int roomId = roomMap.get(roomChoiceBox.getValue());
        List<?> roomResult = db.query("FROM Room WHERE id =" + roomId);
        Room apptRoom = (Room) roomResult.get(0);

        // Create the booking entity
        Booking booking = new Booking();
        booking.setRoom(apptRoom);
        booking.setDoctor(doctor);
        booking.setPatient(currentUser);
        booking.setApptTime(apptDate);

        // booking has been saved
        db.save(booking);

        // clears all of the users choices on booking
        ((DatePicker) root.lookup("#datePicker")).getEditor().clear();
        roomChoiceBox.getSelectionModel().clearSelection();
        docSelection.getSelectionModel().clearSelection();
        textField.clear();
    }

    @Override
    public void cleanup() {
        // nothing to clean up
    }
}
