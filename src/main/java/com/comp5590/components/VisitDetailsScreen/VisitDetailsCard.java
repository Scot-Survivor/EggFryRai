package com.comp5590.components.VisitDetailsScreen;

import com.comp5590.database.entities.Prescription;
import com.comp5590.database.entities.VisitDetails;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class VisitDetailsCard extends VBox {

    public VisitDetailsCard(VisitDetails visitDetails) {
        this.getStylesheets().add("/css/visitDetails.css");

        this.getStyleClass().add("visit-details-card");

        // create header with Visit Timestamp
        Text header = new Text("Visit on " + visitDetails.getTimeAdded().toString());

        // set style class for header
        header.getStyleClass().add("visit-details-header");

        // create Text objects for each field
        Text followUpRequiredText = new Text("Follow Up Required: ");
        Text notesText = new Text("Notes: ");
        Text diagnosisText = new Text("Diagnosis: ");
        Text adviceText = new Text("Advice: ");
        Text timeAddedText = new Text("Time of Visit: ");
        Text bookingIdText = new Text("Booking ID: ");
        Text prescriptionsText = new Text("Prescriptions: ");

        // set style class for above text objects
        followUpRequiredText.getStyleClass().add("field-text");
        notesText.getStyleClass().add("field-text");
        diagnosisText.getStyleClass().add("field-text");
        adviceText.getStyleClass().add("field-text");
        timeAddedText.getStyleClass().add("field-text");
        bookingIdText.getStyleClass().add("field-text");
        prescriptionsText.getStyleClass().add("field-text");

        // create Text objects for each data
        Text followUpRequiredData = new Text(visitDetails.isFollowUpRequired() ? "Yes" : "No");
        Text notesData = new Text(visitDetails.getNotes());
        Text diagnosisData = new Text(visitDetails.getDiagnosis() == null ? "N/A" : visitDetails.getDiagnosis());
        Text adviceData = new Text(visitDetails.getAdvice());
        Text timeAddedData = new Text(visitDetails.getTimeAdded().toString());
        Text bookingIdData = new Text(String.valueOf(visitDetails.getBooking().getBookingId()));
        Text prescriptionsData = new Text("");
        // make prescriptions text...
        for (Prescription prescription : visitDetails.getPrescriptions()) {
            // grab name & recommended dose of each prescription
            String name = prescription.getPrescriptionName();
            String dose = prescription.getRecommendedDose();

            // append to prescriptions text
            prescriptionsData.setText(prescriptionsData.getText() + name + " (" + dose + "), ");
        }

        // if no prescriptions, set text to N/A
        if (prescriptionsData.getText().equals("")) {
            prescriptionsData.setText("N/A");
        }
        // else remove the last comma
        else {
            prescriptionsData.setText(
                prescriptionsData.getText().substring(0, prescriptionsData.getText().length() - 2)
            );
        }

        // make N hboxes for N fields, add corresponding text and data to each hbox
        HBox followUpRequiredBox = new HBox(followUpRequiredText, followUpRequiredData);
        HBox notesBox = new HBox(notesText, notesData);
        HBox diagnosisBox = new HBox(diagnosisText, diagnosisData);
        HBox adviceBox = new HBox(adviceText, adviceData);
        HBox timeAddedBox = new HBox(timeAddedText, timeAddedData);
        HBox bookingIdBox = new HBox(bookingIdText, bookingIdData);
        HBox prescriptionsBox = new HBox(prescriptionsText, prescriptionsData);

        // set class for each hbox
        followUpRequiredBox.getStyleClass().add("field-data-box");
        notesBox.getStyleClass().add("field-data-box");
        diagnosisBox.getStyleClass().add("field-data-box");
        adviceBox.getStyleClass().add("field-data-box");
        timeAddedBox.getStyleClass().add("field-data-box");
        bookingIdBox.getStyleClass().add("field-data-box");
        prescriptionsBox.getStyleClass().add("field-data-box");

        // add all hboxes to this vbox
        this.getChildren()
            .addAll(
                header,
                followUpRequiredBox,
                notesBox,
                diagnosisBox,
                adviceBox,
                timeAddedBox,
                bookingIdBox,
                prescriptionsBox
            );
    }
}
