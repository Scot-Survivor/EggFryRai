package com.comp5590.screens;

import com.comp5590.database.entities.User;
import com.comp5590.enums.UserRole;
import com.comp5590.managers.ScreenManager;
import com.comp5590.security.managers.authentication.annotations.AuthRequired;
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

    private TableView<User> doctorTable;

    public ChooseDoctorScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // Load custom css
        this.addCss("/docList.css");

        // attach default pane, but grab the reference to the gridpane (set as
        // center of borderpane) for further customization
        GridPane pane = this.attachDefaultPane();
        // attach header and nav bar
        this.attachHeaderAndNavBar("Choose Doctor");

        doctorTable = new TableView<>(); // Initialize table
        ((BorderPane) getRootPane()).setCenter(center());
        fillTable(); // Fill table with data
    }

    private VBox center() {
        doctorTable.setId("doctorTable");

        // Create button
        Button switchButton = new Button("Change doctor");
        switchButton.setId("switchButton");
        switchButton.setOnAction(this::docSwitch);

        // Add elements to VBox
        VBox center = new VBox(doctorTable, switchButton);
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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null); // Get rid of header
            alert.setTitle("No selected doctor");
            alert.setContentText("You must select a doctor to change to.");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setId("noSelect"); // Set id for test class
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null); // Get rid of header
            alert.setTitle("Doctor changed");
            alert.setContentText(
                "You have switched your doctor to " +
                changedDoctor.getFirstName() +
                " " +
                changedDoctor.getSurName() +
                "."
            );

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setId("doctorSelected"); // Set id for test class
            alert.showAndWait();
            int doctorId = changedDoctor.getId(); // id of doctor to be used
        }
    }

    @Override
    public void cleanup() {
        doctorTable.getItems().clear();
    }
}
