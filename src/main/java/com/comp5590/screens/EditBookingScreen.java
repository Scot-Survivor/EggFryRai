package com.comp5590.screens;

import com.comp5590.components.EditBooking.DateChoice;
import com.comp5590.components.EditBooking.DoctorChoice;
import com.comp5590.components.EditBooking.EditBookingCard;
import com.comp5590.components.EditBooking.RoomChoice;
import com.comp5590.components.EditBooking.TimeChoice;
import com.comp5590.components.LoginScreen.BigButton;
import com.comp5590.database.entities.Booking;
import com.comp5590.database.entities.Room;
import com.comp5590.database.entities.User;
import com.comp5590.database.utils.EntityUtils;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import com.comp5590.security.authentication.annotations.AuthRequired;
import io.github.palexdev.mfxcore.controls.Label;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.core.Logger;

@AuthRequired
public class EditBookingScreen extends AbstractScreen {

    private final Logger logger = LoggerManager.getInstance().getLogger(EditBookingScreen.class);

    @Getter
    @Setter
    private Booking bookingToEdit;

    // hashmaps for storing doctors and rooms
    private final HashMap<String, User> doctorMap = new HashMap<>();
    private final HashMap<String, Room> roomMap = new HashMap<>();

    // fields for every field that can be filled in for Booking, except booking ID,
    // that the user can fill in
    private ChoiceBox<String> doctorChoiceInput = new ChoiceBox<>();
    private ChoiceBox<String> roomChoiceInput = new ChoiceBox<>();
    private DatePicker dateChoiceInput = new DatePicker();
    private ChoiceBox<String> timeChoiceInput = new ChoiceBox<>();

    // error label
    @Setter
    private Label errorLabel;

    public EditBookingScreen(ScreenManager screenManager) {
        super(screenManager);
        this.errorLabel = new Label("Please fill in all fields to edit booking. Click confirm button to edit booking.");
    }

    @Override
    public void setup() {
        // cleanup
        this.clearMaps();
        this.clearInputFields();

        // Load custom css
        this.addCss("/css/editBooking.css");

        // attach default pane, but grab the reference to the gridpane (set as
        // center of borderpane) for further customization
        GridPane gridPane = this.attachDefaultPane();
        // attach header and nav bar
        this.attachHeaderAndNavBar("Edit Booking");

        // get booking
        Booking oldBooking = this.getBookingToEdit();

        // if booking is null, error, then return to home screen
        if (oldBooking == null) {
            logger.error("No booking to edit, returning to home screen");
            this.showScene(HomeScreen.class);
            return;
        }

        // load all the booking data into boxes with default values, for editing soon
        // (and to be passed into the edit booking card component)
        DoctorChoice doctorChoice = new DoctorChoice(this.doctorChoiceInput, bookingToEdit.getDoctor(), this.doctorMap);
        RoomChoice roomChoice = new RoomChoice(this.roomChoiceInput, bookingToEdit.getRoom(), this.roomMap);
        DateChoice dateChoice = new DateChoice(this.dateChoiceInput, bookingToEdit.getApptTime());
        TimeChoice timeChoice = new TimeChoice(this.timeChoiceInput);

        // create button with ID
        BigButton confirmEditBookingBtn = new BigButton();
        confirmEditBookingBtn.setText("Confirm Edit Booking");
        confirmEditBookingBtn.setId("confirm-edit-booking-btn");
        // on button click, edit booking
        confirmEditBookingBtn.setOnAction(e -> updateBooking());

        // create new booking card, passing in all to-be edited fields (and the button)
        EditBookingCard editBookingCard = new EditBookingCard(
            doctorChoice,
            roomChoice,
            dateChoice,
            timeChoice,
            confirmEditBookingBtn,
            this.errorLabel
        );

        // set card to center of gridpane
        gridPane.add(editBookingCard, 0, 1);
        editBookingCard.setAlignment(javafx.geometry.Pos.CENTER);
    }

    private boolean validateBookingFields() {
        // check if fields are null
        if (
            this.doctorChoiceInput == null ||
            this.roomChoiceInput == null ||
            this.dateChoiceInput == null ||
            this.timeChoiceInput == null
        ) {
            this.errorLabel.setText("Please fill in all fields to edit booking. Click confirm button to edit booking.");
            // set error label to red and bold
            this.errorLabel.setId("error-label");
            return false;
        }

        // check if all fields are filled in
        if (
            this.doctorChoiceInput.getValue() == null ||
            this.roomChoiceInput.getValue() == null ||
            this.dateChoiceInput.getValue() == null ||
            this.timeChoiceInput.getValue() == null
        ) {
            this.errorLabel.setText("Please fill in all fields to edit booking. Click confirm button to edit booking.");
            // set error label to red and bold
            this.errorLabel.setId("error-label");
            return false;
        }

        // validate field integrity
        // check if date is in the future
        if (this.dateChoiceInput.getValue().isBefore(java.time.LocalDate.now())) {
            this.errorLabel.setText("Please select a date in the future.");
            // set error label to red and bold
            this.errorLabel.setId("error-label");
            return false;
        }

        // check if time is in the future
        if (
            this.dateChoiceInput.getValue().isEqual(java.time.LocalDate.now()) &&
            java.time.LocalTime.parse(this.timeChoiceInput.getValue()).isBefore(java.time.LocalTime.now())
        ) {
            this.errorLabel.setText("Please select a time in the future.");
            // set error label to red and bold
            this.errorLabel.setId("error-label");
            return false;
        }

        return true;
    }

    private void updateBooking() {
        // validate fields
        if (!validateBookingFields()) {
            return;
        }

        // grab old booking ID
        int oldBookingID = this.bookingToEdit.getBookingId();

        // grab all input fields
        User doctor = this.doctorMap.get(this.doctorChoiceInput.getValue());
        Room room = this.roomMap.get(this.roomChoiceInput.getValue());
        Date date = java.sql.Date.valueOf(this.dateChoiceInput.getValue());

        // grab time string and convert to date, then add to date
        String timeStr = this.timeChoiceInput.getValue();
        // parse time (e.g., 13:00) to date
        LocalTime parsedTime = LocalTime.parse(timeStr);
        Date time = new Date(parsedTime.getHour() * 60 * 60 * 1000 + parsedTime.getMinute() * 60 * 1000);

        // grab both date and time in MS
        long dateMS = date.getTime();
        long timeMS = time.getTime();
        // combine date and time into one datetime object
        Date apptTime = new Date(dateMS + timeMS);

        // fill in new booking with old booking ID & user, but new booking fields (now
        // validated)
        Booking newBooking = new Booking();
        newBooking.setBookingId(oldBookingID);
        newBooking.setDoctor(doctor);
        newBooking.setPatient(this.bookingToEdit.getPatient());
        newBooking.setApptTime(apptTime);
        newBooking.setRoom(room);

        // update booking in db
        EntityUtils.updateBooking(newBooking);

        // send notificaton to both user and doctor
        // for user
        EntityUtils.createNotification(
            "Your booking of ID " +
            oldBookingID +
            " has updated. New Doctor: " +
            doctor.getFirstName() +
            " " +
            doctor.getSurName() +
            ". New Room: " +
            room.getRoomNumber() +
            ". New Date: " +
            date +
            ". New Time: " +
            time +
            ".",
            this.bookingToEdit.getPatient()
        );

        // for doctor
        EntityUtils.createNotification(
            "You have a new booking of ID " +
            oldBookingID +
            ". New Patient: " +
            this.bookingToEdit.getPatient().getFirstName() +
            " " +
            this.bookingToEdit.getPatient().getSurName() +
            ". New Room: " +
            room.getRoomNumber() +
            ". New Date: " +
            date +
            ". New Time: " +
            time +
            ".",
            doctor
        );

        // return to view and change bookings screen
        this.showSceneBetweenScenesThenNextScene("Booking updated successfully!", ViewAndChangeBookingsScreen.class);
    }

    @Override
    public void cleanup() {
        // delete bookings field
        this.bookingToEdit = null;

        // clear maps
        this.doctorMap.clear();
        this.roomMap.clear();

        // clear all input fields
        this.doctorChoiceInput.setValue(null);
        this.roomChoiceInput.setValue(null);
        this.dateChoiceInput.setValue(null);
        this.timeChoiceInput.setValue(null);

        // delete error label
        this.errorLabel.setText("Please fill in all fields to edit booking. Click confirm button to edit booking.");
    }

    public void clearMaps() {
        this.doctorMap.clear();
        this.roomMap.clear();
    }

    public void clearInputFields() {
        this.doctorChoiceInput = new ChoiceBox<>();
        this.roomChoiceInput = new ChoiceBox<>();
        this.dateChoiceInput = new DatePicker();
        this.timeChoiceInput = new ChoiceBox<>();
    }
}
