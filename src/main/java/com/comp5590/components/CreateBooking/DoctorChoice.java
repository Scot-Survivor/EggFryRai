package com.comp5590.components.CreateBooking;

import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.enums.UserRole;
import com.comp5590.managers.LoggerManager;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.apache.logging.log4j.core.Logger;

public class DoctorChoice extends VBox {

    @Getter
    private final ChoiceBox<String> choiceBox;

    private final Logger logger;

    public DoctorChoice(DatabaseManager db, HashMap<String, User> doctorMap) {
        logger = LoggerManager.getInstance().getLogger(DoctorChoice.class);
        // Create the label for the doctor choice box
        Label doctorLabel = new Label("Please select a doctor");
        VBox.setMargin(doctorLabel, new Insets(10.0));
        choiceBox = new ChoiceBox<>();
        choiceBox.setPrefWidth(150.0);
        choiceBox.setId("doctorChoiceBox"); // give the choice box an id

        // Grab a list of doctors
        List<User> docList = db.getAllByProperty(User.class, "role", UserRole.DOCTOR);

        // Attempt to add all doctors to the drop down. If fails then just print message and don't display and doctors
        try {
            for (User doc : docList) {
                String docName = doc.getFirstName() + " " + doc.getSurName();
                choiceBox.getItems().add(docName);
                doctorMap.put(docName, doc);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.debug(e.getStackTrace());
        }

        // create the VBox to store these items and then return it
        VBox doctorBox = new VBox(doctorLabel, choiceBox);
        doctorBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);

        this.getChildren().add(doctorBox);
    }
}
