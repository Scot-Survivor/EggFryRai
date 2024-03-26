package com.comp5590.screens;

import com.comp5590.components.LoginScreen.Title;
import com.comp5590.database.entities.User;
import com.comp5590.managers.ScreenManager;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class DocListScreen extends AbstractScreen {

    private TableView<User> doctorTable;

    public DocListScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // Load custom css
        this.addCss("/docList.css");

        setRootPane(new BorderPane());
        doctorTable = new TableView<>(); // Initialize table
        ((BorderPane) getRootPane()).setCenter(center());
        fillTable(); // Fill table with data

        // add navigation buttons
        addBackAndHomeButtons(getRootPane());
    }

    private VBox center() {
        doctorTable.setId("doctorTable");
        // Create title
        HBox titleBox = new Title("Select a doctor");
        titleBox.setId("title");

        // Create button
        Button switchButton = new Button("Change doctor");
        switchButton.setId("switchButton");
        switchButton.setOnAction(this::docSwitch);

        // Add elements to VBox
        VBox center = new VBox(titleBox, doctorTable, switchButton);
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
        List<?> doctorQuery = getDatabaseManager()
            .query("SELECT Id, firstName, surName, phone, additionalNotes FROM User WHERE role = 'DOCTOR'");
        List<User> doctors = new ArrayList<>();

        // Iterate through query and map to user list
        for (Object result : doctorQuery) {
            Object[] doctorFields = (Object[]) result;
            User doctor = new User();
            doctor.setId((Integer) doctorFields[0]);
            doctor.setFirstName(doctorFields[1].toString());
            doctor.setSurName(doctorFields[2].toString());
            doctor.setPhone(doctorFields[3].toString());
            doctor.setAdditionalNotes(doctorFields[4].toString());
            doctors.add(doctor);
        }

        return doctors;
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
