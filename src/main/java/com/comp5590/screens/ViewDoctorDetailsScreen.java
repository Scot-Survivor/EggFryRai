package com.comp5590.screens;

import com.comp5590.components.DoctorDetailsScreen.DoctorChoice;
import com.comp5590.components.DoctorDetailsScreen.DoctorDisplayBox;
import com.comp5590.components.DoctorDetailsScreen.Title;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.managers.ScreenManager;
import com.comp5590.security.managers.authentication.annotations.AuthRequired;
import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

// TODO: Add spacing between the items
// TODO: Make the items look pretty
// TODO: Write tests for the screen

@AuthRequired
public class ViewDoctorDetailsScreen extends AbstractScreen {

    private HashMap<String, User> doctorMap;

    public ViewDoctorDetailsScreen(ScreenManager screenManager) {
        super(screenManager);
        doctorMap = new HashMap<>();
    }

    @Override
    public void setup() {
        BorderPane rootPane = new BorderPane();
        setRootPane(rootPane);
        rootPane.setCenter(createCenter());
        new Title(rootPane);
    }

    private BorderPane createCenter() {
        BorderPane centralPane = new BorderPane();
        DatabaseManager db = DatabaseManager.getInstance();
        VBox vbox = new VBox();

        new DoctorChoice(db, doctorMap, vbox);

        Button showDoctor = new Button("Show");
        VBox.setMargin(showDoctor, new Insets(10, 10, 10, 10));
        showDoctor.setOnAction(e -> {
            BorderPane rootPane = (BorderPane) getRootPane();

            // get the choice of doctor to look at
            ChoiceBox<String> doc = (ChoiceBox<String>) rootPane.lookup("#doctorChoiceBox");
            User doctor = doctorMap.get(doc.getValue());

            // get all of the details and format
            String fullName = "Name: " + doctor.getFirstName() + " " + doctor.getSurName() + "\n";
            String number = "Phone no: " + doctor.getPhone() + "\n";
            String email = "Email Address: " + doctor.getAuthenticationDetails().getEmail() + "\n";
            String fax = "Fax no: " + doctor.getFax() + "\n";
            String contactPreference = "Contact Preference: " + doctor.getCommunicationPreference() + "\n";
            String addNotes = "Additional Notes: " + doctor.getAdditionalNotes() + "\n";

            // find the box to display
            BorderPane root = (BorderPane) getRootPane();
            VBox doctorInfo = (VBox) root.lookup("#docDisplay");

            // clear prior contents
            doctorInfo.getChildren().clear();

            // add to the box
            doctorInfo
                .getChildren()
                .addAll(
                    new Text(fullName),
                    new Text(number),
                    new Text(email),
                    new Text(fax),
                    new Text(contactPreference),
                    new Text(addNotes)
                );
        });

        vbox.getChildren().addAll(showDoctor);

        new DoctorDisplayBox(vbox);

        vbox.setAlignment(Pos.TOP_CENTER);
        centralPane.setCenter(vbox);

        return centralPane;
    }

    @Override
    public void cleanup() {
        // Nothing to cleanup
    }
}
