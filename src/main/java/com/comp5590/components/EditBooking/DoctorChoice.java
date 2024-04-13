package com.comp5590.components.EditBooking;

import com.comp5590.database.entities.User;
import com.comp5590.database.utils.EntityUtils;
import io.github.palexdev.mfxcore.controls.Label;
import java.util.HashMap;
import java.util.List;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;

public class DoctorChoice extends HBox {

    public DoctorChoice(ChoiceBox<String> doctorChoiceInput, User defaultDoctor, HashMap<String, User> doctorMap) {
        // import styling
        this.getStylesheets().add("/css/editBooking.css");

        // add styling
        this.getStyleClass().add("doctor-choice-box");

        // grab all doctors from DB
        List<User> doctors = EntityUtils.getAllDoctors();

        // create label
        Label doctorLabel = new Label("Please select a doctor");
        // create choice box
        doctorChoiceInput.setId("doctor-choice");

        // add all doctors to the choice box
        for (User doctor : doctors) {
            String docIdentifier = String.format(
                "%s%s_(%d)",
                doctor.getFirstName(),
                doctor.getSurName(),
                doctor.getId()
            );

            doctorMap.put(docIdentifier, doctor);

            doctorChoiceInput.getItems().add(docIdentifier);
        }

        // set the default value to the default doctor
        doctorChoiceInput.setValue(
            String.format("%s%s_(%d)", defaultDoctor.getFirstName(), defaultDoctor.getSurName(), defaultDoctor.getId())
        );

        // add label & choice box to the HBox
        this.getChildren().addAll(doctorLabel, doctorChoiceInput);
    }
}
