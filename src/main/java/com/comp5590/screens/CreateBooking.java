package com.comp5590.screens;

// TODO: Make it consistent with the design schema

import com.comp5590.components.CreateBooking.*;
import com.comp5590.database.entities.Booking;
import com.comp5590.database.entities.Room;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.enums.UserRole;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import com.comp5590.managers.SessionManager;
import com.comp5590.security.managers.authentication.annotations.AuthRequired;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.core.Logger;

@AuthRequired
public class CreateBooking extends AbstractScreen {

    private final HashMap<String, User> doctorMap;
    private final HashMap<String, Room> roomMap;
    private final Logger logger = LoggerManager.getInstance().getLogger(CreateBooking.class);

    private AppointmentReason appointmentReason;
    private DatePickerBox datePickerBox;
    private DoctorChoice doctorChoice;
    private RoomChoice roomChoice;
    private WarningMessage warningMessage;

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
        new Title(rootPane);
        rootPane.setCenter(createCenter());
        addBackAndHomeButtons(rootPane); // Add back and home buttons to the page
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

        // Add all the sub items to the VBox
        vBox.getChildren().add(createBookingButton());
        centerPane.setCenter(vBox);

        // return the central pane of the page
        return centerPane;
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
        book.setId("bookingButton");

        return book;
    }

    /**
     * The functionality to allow the booking of an event
     * @param event The event that triggers the booking
     */
    private void book(ActionEvent event) {
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

        // Get the appt date
        LocalDate datePicker = datePickerBox.getDatePicker().getValue();
        java.sql.Date apptDate = java.sql.Date.valueOf(datePicker);

        ChoiceBox<String> timeBox = datePickerBox.getChoiceBox();
        String time = timeBox.getValue();
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDate localDate = apptDate.toLocalDate();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        Date dateWithTime = new Date(timestamp.getTime());
        System.err.println(dateWithTime);

        // Parse the time string "08:00"
        // String timeString = "08:00";
        // LocalTime time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
        // Combine the date without time and the parsed time to create a LocalDateTime object
        // LocalDateTime dateTime = LocalDateTime.of(LocalDate.from(dateWithoutTime.toInstant()), time);
        // Convert LocalDateTime to Date
        // Date dateWithTime = Date.from(dateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        // get the current user
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser.getRole() != UserRole.PATIENT) {
            logger.error("User is not a patient");
            return;
        }

        // get the users current room choice
        ChoiceBox<String> roomChoiceBox = roomChoice.getChoiceBox();
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
        Logic is if we get all appointments at a given time then if any other thing like doctor or room are the same
        then we know it is not possible to save as they are duplicates
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
            db.save(booking);
            warningText.setText("Making Appointment");
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
        // nothing to clean up
    }
}
