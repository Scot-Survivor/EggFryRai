package com.comp5590.screens.doctors;

import com.comp5590.database.entities.User;
import com.comp5590.database.utils.EntityUtils;
import com.comp5590.enums.UserRole;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.managers.ScreenManager;
import com.comp5590.screens.misc.AbstractScreen;
import com.comp5590.security.authentication.annotations.AuthRequired;
import java.util.List;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
@AuthRequired
public class ChooseDoctorScreen extends AbstractScreen {

    private TableView<User> doctorTable = new TableView<>();
    private Label resultLabel;
    private Button switchButton;

    public ChooseDoctorScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // Load custom css
        this.addCss("/css/chooseDoctor.css");

        // attach default pane, but grab the reference to the gridpane (set as
        // center of borderpane) for further customization
        GridPane pane = this.attachDefaultPane();
        // attach header and nav bar
        this.attachHeaderAndNavBar(1, "Choose Doctor");

        doctorTable = new TableView<>(); // Initialize table
        ((BorderPane) getRootPane()).setCenter(center());
        fillTable(); // Fill table with data
    }

    private VBox center() {
        doctorTable.setId("doctorTable");

        // Create button
        switchButton = new Button("Change doctor");
        switchButton.setId("switchButton");
        switchButton.setOnAction(this::docSwitch);

        resultLabel = new Label(""); // Set as empty
        resultLabel.setId("resultLabel");

        // Add elements to VBox
        VBox center = new VBox(doctorTable, switchButton, resultLabel);
        center.setId("center");
        center.getStyleClass().add("custom-pane");
        center.setPrefSize(600, 400);
        // set max width to 250, and max height to fit content
        center.setMaxSize(600, 600);
        center.setAlignment(Pos.CENTER);
        return center;
    }

    /**
     * Method that does query and fills table with query results
     */
    public void fillTable() {
        List<User> doctorData = getDoctors();
        createTable(doctorData);
    }

    /**
     * Method to call query on database to retrieve list of doctors
     *
     * @return list of doctors with parameters: firstName, surName, phone
     */
    private List<User> getDoctors() {
        return getDatabaseManager().getAllByProperty(User.class, "role", UserRole.DOCTOR);
    }

    /**
     * Create table and input data into it
     *
     * @param data The results of the query
     */
    private void createTable(List<User> data) {
        doctorTable.getColumns().clear();

        // Create and map full name column
        TableColumn<User, String> fullNameColumn = new TableColumn<>("Full name");
        fullNameColumn.setCellValueFactory(cellData -> {
            String firstName = cellData.getValue().getFirstName();
            String surName = cellData.getValue().getSurName();
            return new ReadOnlyStringWrapper(firstName + " " + surName);
        });
        fullNameColumn.setPrefWidth(180);

        // Create and map phone number column
        TableColumn<User, String> phoneColumn = new TableColumn<>("Phone number");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneColumn.setPrefWidth(180);

        // Create and map additional notes column
        TableColumn<User, String> additionalNotesColumn = new TableColumn<>("Additional Notes");
        additionalNotesColumn.setCellValueFactory(new PropertyValueFactory<>("additionalNotes"));
        additionalNotesColumn.setPrefWidth(180);

        // Add columns to table
        doctorTable.getColumns().add(fullNameColumn);
        doctorTable.getColumns().add(phoneColumn);
        doctorTable.getColumns().add(additionalNotesColumn);

        // Add data to table
        ObservableList<User> viewingData = FXCollections.observableArrayList(data);
        doctorTable.setItems(viewingData);

        // Create tooltips
        doctorTable.setRowFactory(TableView -> {
            TableRow<User> hoveredRow = new TableRow<>();
            hoveredRow.setOnMouseEntered(event -> {
                if (hoveredRow.getItem() != null) {
                    User doctor = hoveredRow.getItem();
                    Tooltip details = new Tooltip(
                        "Doctor Details: \n" +
                        "Name: " +
                        doctor.getFirstName() +
                        " " +
                        doctor.getSurName() +
                        "\n" +
                        "Email: " +
                        doctor.getAuthenticationDetails().getEmail() +
                        "\n" +
                        "Phone: " +
                        doctor.getPhone() +
                        "\n" +
                        "Fax: " +
                        doctor.getFax() +
                        "\n" +
                        "Additional Notes: " +
                        doctor.getAdditionalNotes()
                    );
                    Tooltip.install(hoveredRow, details); // Add tooltip
                }
            });

            hoveredRow.setOnMouseExited(event -> { // Remove tooltip when mouse leaves
                Tooltip.uninstall(hoveredRow, hoveredRow.getTooltip());
            });
            return hoveredRow;
        });
    }

    /**
     * Notify user about switch result, get id of doctor
     *
     * @param event on button click
     */
    private void docSwitch(ActionEvent event) {
        User changedDoctor = doctorTable.getSelectionModel().getSelectedItem();
        if (changedDoctor == null) { // No doctor selected
            // Set label
            resultLabel.setText("You must select a doctor to change to.");
            resultLabel.setStyle("-fx-text-fill: red");
        } else {
            // send notificaton to both user and doctor
            SessionManager seshManager = SessionManager.getInstance();
            User currentUser = seshManager.getCurrentUser();

            // send notifications to user
            EntityUtils.createNotification(
                "You have switched your doctor to " +
                changedDoctor.getFirstName() +
                " " +
                changedDoctor.getSurName() +
                ".",
                currentUser
            );

            // send notifications to doctor
            EntityUtils.createNotification(
                "You have a new patient, " + currentUser.getFirstName() + " " + currentUser.getSurName() + ".",
                changedDoctor
            );

            // Set label
            resultLabel.setText(
                "You have switched your doctor to " +
                changedDoctor.getFirstName() +
                " " +
                changedDoctor.getSurName() +
                "."
            );
            resultLabel.setStyle("-fx-text-fill: green");

            int doctorId = changedDoctor.getId(); // id of doctor to be used
        }
    }

    @Override
    public void cleanup() {
        doctorTable.getItems().clear();
    }
}
