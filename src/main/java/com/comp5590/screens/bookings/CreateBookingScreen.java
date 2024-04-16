package com.comp5590.screens.bookings;

// TODO: Make it consistent with the design schema

import com.comp5590.components.CreateBooking.*;
import com.comp5590.database.entities.Booking;
import com.comp5590.database.entities.Room;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.database.utils.EntityUtils;
import com.comp5590.enums.UserRole;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.managers.ScreenManager;
import com.comp5590.screens.misc.AbstractScreen;
import com.comp5590.security.authentication.annotations.AuthRequired;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.core.Logger;

@AuthRequired
public class CreateBookingScreen extends AbstractScreen {

    private final HashMap<String, User> doctorMap;
    private final HashMap<String, Room> roomMap;
    private final Logger logger = LoggerManager.getInstance().getLogger(CreateBookingScreen.class);

    private AppointmentReason appointmentReason;
    private DatePickerBox datePickerBox;
    private DoctorChoice doctorChoice;
    private RoomChoice roomChoice;
    private WarningMessage warningMessage;

    public CreateBookingScreen(ScreenManager screenManager) {
        super(screenManager);
        doctorMap = new HashMap<>();
        roomMap = new HashMap<>();
    }

    @Override
    public void setup() {
        // get the current user for later use
        // currentUser = App.getInstance().getCurrentUser();

        // attach default pane, but grab the reference to the gridpane (set as
        // center of borderpane) for further customization
        GridPane pane = this.attachDefaultPane();
        // attach header and nav bar
        this.attachHeaderAndNavBar(1, "Booking Appointment");

        ((BorderPane) getRootPane()).setCenter(this.createCenter());
    }

    /**
     * Create the center items of the bookings screen
     *
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

        DatabaseManager db = getDatabaseManager();

        // Add components to the screen
        appointmentReason = new AppointmentReason();
        VBox.setMargin(appointmentReason, new Insets(20.0));
        vBox.getChildren().add(appointmentReason);

        Label dateLabel = new Label("Select a date for your appointment");
        VBox.setMargin(dateLabel, new Insets(10.0));
        datePickerBox = new DatePickerBox();
        vBox.getChildren().add(dateLabel);
        vBox.getChildren().add(datePickerBox);

        doctorChoice = new DoctorChoice(db, doctorMap);
        vBox.getChildren().add(doctorChoice);
        roomChoice = new RoomChoice(db, roomMap);
        vBox.getChildren().add(roomChoice);
        warningMessage = new WarningMessage();
        VBox.setMargin(warningMessage, new Insets(20.0));
        vBox.getChildren().add(warningMessage);

        // create booking button
        BookingButton bookingButton = new BookingButton();
        // attach event handler to the button
        bookingButton.setOnAction(this::book);
        bookingButton.setId("bookingButton");

        // Add all the sub items to the VBox
        vBox.getChildren().add(bookingButton);
        centerPane.setCenter(vBox);

        // return the central pane of the page
        return centerPane;
    }

    /**
     * The functionality to allow the booking of an event
     *
     * @param event The event that triggers the booking
     */
    private void book(ActionEvent event) {
        // get the current user
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser.getRole() != UserRole.PATIENT) {
            logger.error("User is not a patient");
            return;
        }

        // open a db session
        DatabaseManager db = getDatabaseManager();
        Pane root = getRootPane(); // get the root pane

        // Grab the text field
        MFXTextField textField = (MFXTextField) root.lookup("#apptReasonTextField");

        // get the id of the doctor
        ChoiceBox<String> docSelection = doctorChoice.getChoiceBox();
        User doc = doctorMap.get(docSelection.getValue());

        // get the apt reason
        String apptReason = appointmentReason.getText();

        // error handling for if the reason is null
        if (apptReason == null || apptReason.isEmpty()) {
            logger.error("Appointment reason is null");
            warningMessage.setText("Please enter a reason for the appointment");
            return;
        }

        // Get the appt date
        LocalDate datePicker = datePickerBox.getDatePicker().getValue();

        // error handling for if the date is null
        if (datePicker == null) {
            logger.error("Booking date is null");
            warningMessage.setText("Please select a date");
            return;
        }

        // error handling if no time is selected
        if (datePickerBox.getChoiceBox().getValue() == null) {
            logger.error("Booking time is null");
            warningMessage.setText("Please select a time");
            return;
        }

        java.sql.Date apptDate = java.sql.Date.valueOf(datePicker);

        ChoiceBox<String> timeBox = datePickerBox.getChoiceBox();
        String time = timeBox.getValue();
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDate localDate = apptDate.toLocalDate();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        Date dateWithTime = new Date(timestamp.getTime());
        System.err.println(dateWithTime);

        // get the users current room choice
        ChoiceBox<String> roomChoiceBox = roomChoice.getChoiceBox();

        // if no room is selected
        if (roomChoiceBox.getValue() == null) {
            logger.error("Room is null");
            warningMessage.setText("Please select a room");
            return;
        }

        Room room = roomMap.get(roomChoiceBox.getValue());

        // Create the booking entity
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setDoctor(doc);
        booking.setPatient(currentUser);
        booking.setApptTime(dateWithTime);

        // TODO: Make sure the doctor, room and time are not the same

        boolean okToSave = true; // if this gets set to false then we cant save

        /*
         * Logic is if we get all appointments at a given time then if any other thing
         * like doctor or room are the same
         * then we know it is not possible to save as they are duplicates
         */

        // Gets all appointments at our given time
        List<Booking> bookings = db.getAllByProperty(Booking.class, "apptTime", dateWithTime);
        for (Booking appt : bookings) { // Check over each booking
            if (appt.getDoctor().equals(doc)) { // Check if it's the same doctor at that time
                logger.warn("SAME DOCTOR DONT SAVE");
                okToSave = false;
            } else if (appt.getRoom().equals(room)) {
                logger.warn("SAME ROOM DONT SAVE");
                okToSave = false;
            }
        }

        Text warningText = (Text) root.lookup("#warningMessage");
        warningText.setText("");

        // Save if ok
        if (okToSave) {
            // booking has been saved
            db.saveGet(booking);
            warningText.setText("Making Appointment");

            // send notificaton to both user and doctor
            EntityUtils.createNotification(
                "You have booked an appointment of ID " +
                booking.getBookingId() +
                " with " +
                doc.getFirstName() +
                " " +
                doc.getSurName() +
                " for " +
                apptReason +
                " at " +
                dateWithTime,
                currentUser
            );

            // send notifications to doctor
            EntityUtils.createNotification(
                "You have a new appointment with " +
                currentUser.getFirstName() +
                " " +
                currentUser.getSurName() +
                " for " +
                apptReason +
                " at " +
                dateWithTime,
                doc
            );
        } else {
            logger.warn("Cannot make appointment");
            warningText.setText("Cannot make appointment");
        }

        // clears all of the users choices on booking
        datePickerBox.getDatePicker().getEditor().clear();
        roomChoiceBox.getSelectionModel().clearSelection();
        docSelection.getSelectionModel().clearSelection();
        timeBox.getSelectionModel().clearSelection();
        textField.clear();
    }

    @Override
    public void cleanup() {
        // clear the doctor map
        doctorMap.clear();
        // clear the room map
        roomMap.clear();
    }
}
