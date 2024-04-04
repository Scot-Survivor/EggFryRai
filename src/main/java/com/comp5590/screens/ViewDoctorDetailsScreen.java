package com.comp5590.screens;

import com.comp5590.components.DoctorDetailsScreen.DoctorChoice;
import com.comp5590.components.DoctorDetailsScreen.DoctorDisplayBox;
import com.comp5590.components.DoctorDetailsScreen.Title;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import com.comp5590.security.managers.authentication.annotations.AuthRequired;
import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.core.Logger;

@AuthRequired
public class ViewDoctorDetailsScreen extends AbstractScreen {

    private HashMap<String, User> doctorMap; // Will contain a mapping of doctors names to their object
    private final Logger logger = LoggerManager.getInstance().getLogger(ViewDoctorDetailsScreen.class);

    public ViewDoctorDetailsScreen(ScreenManager screenManager) {
        super(screenManager);
        doctorMap = new HashMap<>();
    }

    @Override
    public void setup() {
        BorderPane rootPane = new BorderPane();
        setRootPane(rootPane);
        rootPane.setCenter(createCenter());
        rootPane.setTop(new Title()); // Add title to the page
        addBackAndHomeButtons(rootPane); // Add back and home buttons to the page
    }

    /**
     * Create the central pane of the root pane
     * @return A BorderPane containing what all what was added
     */
    private BorderPane createCenter() {
        BorderPane centralPane = new BorderPane();
        DatabaseManager db = DatabaseManager.getInstance();
        VBox vbox = new VBox(); // To contain all elements

        // Create components to add to the screen
        DoctorChoice docChoice = new DoctorChoice(db, doctorMap);
        DoctorDisplayBox docDisplay = new DoctorDisplayBox();

        // Create the button and add the functionality
        Button showDoctor = new Button("Show");
        showDoctor.setId("showButton");
        VBox.setMargin(showDoctor, new Insets(10, 10, 10, 10));
        showDoctor.setOnAction(e -> {
            Pane rootPane = getRootPane(); // get root pane for any lookups
            VBox doctorInfo = (VBox) rootPane.lookup("#docDisplay"); // get now to save multiple calls
            // clear prior contents
            doctorInfo.getChildren().clear();

            try {
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
            } catch (Exception error) {
                logger.error("No doctor selected | " + error);
                doctorInfo.getChildren().add(new Text("Please select a doctor"));
            }
        });

        // Add all components to the screen
        vbox.getChildren().addAll(docChoice, showDoctor, docDisplay);
        vbox.setAlignment(Pos.TOP_CENTER);
        centralPane.setCenter(vbox);
        return centralPane;
    }

    @Override
    public void cleanup() {
        // Nothing to cleanup
    }
}