package com.comp5590.components.DoctorDetailsScreen;

import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.enums.UserRole;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DoctorChoice extends VBox {

    public DoctorChoice(DatabaseManager db, HashMap<String, User> doctorMap) {
        // Create the label for the doctor choice box
        Label doctorLabel = new Label("Please select a doctor");
        VBox.setMargin(doctorLabel, new Insets(10.0));
        ChoiceBox<String> doctorChoiceBox = new ChoiceBox<>();
        doctorChoiceBox.setPrefWidth(150.0);
        doctorChoiceBox.setId("doctorChoiceBox"); // give the choice box an id

        // Grab a list of doctors
        List<User> docList = db.getAllByProperty(User.class, "role", UserRole.DOCTOR);

        // Attempt to add all doctors to the drop down. If fails then just print message and don't display and doctors
        try {
            for (User doc : docList) {
                String docName = doc.getFirstName() + " " + doc.getSurName();
                doctorChoiceBox.getItems().add(docName);
                doctorMap.put(docName, doc);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // create the VBox to store these items and then return it
        this.getChildren().addAll(doctorLabel, doctorChoiceBox);
        this.setAlignment(javafx.geometry.Pos.TOP_CENTER);
    }
}
