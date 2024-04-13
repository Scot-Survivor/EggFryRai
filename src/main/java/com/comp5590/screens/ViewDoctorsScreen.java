package com.comp5590.screens;

import com.comp5590.components.DoctorDetailsScreen.DoctorChoice;
import com.comp5590.components.DoctorDetailsScreen.DoctorDisplayBox;
import com.comp5590.database.entities.Booking;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import com.comp5590.security.authentication.annotations.AuthRequired;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.core.Logger;

@AuthRequired
public class ViewDoctorsScreen extends AbstractScreen {

    private HashMap<String, User> doctorMap; // Will contain a mapping of doctors names to their object
    private final Logger logger = LoggerManager.getInstance().getLogger(ViewDoctorsScreen.class);

    public ViewDoctorsScreen(ScreenManager screenManager) {
        super(screenManager);
        doctorMap = new HashMap<>();
    }

    @Override
    public void setup() {
        // attach default pane, but grab the reference to the gridpane (set as
        // center of borderpane) for further customization
        GridPane pane = this.attachDefaultPane();
        // attach header and nav bar
        this.attachHeaderAndNavBar("View Doctors");

        // create the page contents, setting them to center of root pane
        ((BorderPane) getRootPane()).setCenter(createCenter());
    }

    /**
     * Create the central pane of the root pane
     *
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

                // Check to see if the doctor has any availability
                // in a month there are a max of 31 days clinic runs from
                // 31*12 "if there are 12 hour days" = 372
                // if we do 30 min appts 372*2=744 doctor could have 744 appts in a month

                List<Booking> bookings = db.getAllByProperty(Booking.class, "doctor", doctor);

                // get a start and end date for the current month
                Date firstDate = new Date();
                // Create a calendar instance and add 31 days
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(firstDate);
                calendar.add(Calendar.DATE, 31);
                Date secondDate = calendar.getTime();

                // Add all bookings in a given month here
                ArrayList<Booking> inMonth = new ArrayList<>();
                for (Booking booking : bookings) {
                    if (booking.getApptTime().before(secondDate) && booking.getApptTime().after(firstDate)) {
                        inMonth.add(booking);
                    }
                }

                String apptAvailability =
                    "The doctor has " + (742 - inMonth.size()) + " appointments available in the current month";

                // add to the box
                doctorInfo
                    .getChildren()
                    .addAll(
                        new Text(fullName),
                        new Text(number),
                        new Text(email),
                        new Text(fax),
                        new Text(contactPreference),
                        new Text(addNotes),
                        new Text(apptAvailability)
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
