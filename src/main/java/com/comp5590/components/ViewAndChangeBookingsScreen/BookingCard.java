package com.comp5590.components.ViewAndChangeBookingsScreen;

import com.comp5590.database.entities.Booking;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class BookingCard extends VBox {

    public BookingCard(Booking booking, Button editButton, Button deleteButton) {
        // add custom css
        this.getStylesheets().add("/viewAndChangeBookingsScreen.css");

        this.getStyleClass().add("booking-card");

        // create booking fields
        // create text objects for each field
        // create text objects for each data
        Text bookingIdText = new Text("Booking ID: ");
        Text doctorNameText = new Text("Doctor Name: ");
        Text patientNameText = new Text("Patient Name: ");
        Text apptTimeText = new Text("Appointment Time: ");
        Text roomNameText = new Text("Room Name: ");

        // grab all booking data
        Text bookingId = new Text(String.valueOf(booking.getBookingId()));
        Text doctorName = new Text(booking.getDoctor().getFirstName() + " " + booking.getDoctor().getSurName());
        Text patientName = new Text(booking.getPatient().getFirstName() + " " + booking.getPatient().getSurName());
        Text apptTime = new Text(booking.getApptTime().toString());
        Text roomName = new Text(booking.getRoom().getRoomNumber());

        // add styling toa bove data
        bookingId.getStyleClass().add("data-text");
        doctorName.getStyleClass().add("data-text");
        patientName.getStyleClass().add("data-text");
        apptTime.getStyleClass().add("data-text");
        roomName.getStyleClass().add("data-text");

        // match field and data into their own HBox
        HBox bookingIdHBox = new HBox(bookingIdText, bookingId);
        HBox doctorNameHBox = new HBox(doctorNameText, doctorName);
        HBox patientNameHBox = new HBox(patientNameText, patientName);
        HBox apptTimeHBox = new HBox(apptTimeText, apptTime);
        HBox roomNameHBox = new HBox(roomNameText, roomName);

        // add 1 style to all above hboxes
        bookingIdHBox.getStyleClass().add("data-hbox");
        doctorNameHBox.getStyleClass().add("data-hbox");
        patientNameHBox.getStyleClass().add("data-hbox");
        apptTimeHBox.getStyleClass().add("data-hbox");
        roomNameHBox.getStyleClass().add("data-hbox");

        // add buttons to a hbox
        HBox buttonHBox = new HBox(editButton, deleteButton);
        buttonHBox.getStyleClass().add("button-hbox");
        editButton.getStyleClass().add("edit-button");
        deleteButton.getStyleClass().add("delete-button");

        // add all vboxes to the card
        this.getChildren()
            .addAll(bookingIdHBox, doctorNameHBox, patientNameHBox, apptTimeHBox, roomNameHBox, buttonHBox);
    }
}
