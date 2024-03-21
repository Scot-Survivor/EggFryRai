package com.comp5590.screens;

import com.comp5590.components.LoginScreen.Title;
import com.comp5590.database.entities.User;
import com.comp5590.managers.ScreenManager;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
        setRootPane(new BorderPane());
        doctorTable = new TableView<>(); // Initialize table
        ((BorderPane) getRootPane()).setCenter(center());
        fillTable(); // Fill table with data
    }

    private VBox center() {
        doctorTable.setId("doctorTable");
        // Create title
        HBox titleBox = new Title("Select a doctor");

        // Create button
        Button switchButton = new Button("Change doctor");
        return new VBox(titleBox, doctorTable, switchButton);
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
     * @return list of doctors with parameters: firstName, surName, phone
     */
    private List<User> getDoctors() {
        List<?> doctorQuery = getDatabaseManager()
            .query("SELECT firstName, surName, phone FROM User WHERE role = 'DOCTOR'");
        List<User> doctors = new ArrayList<>();

        // Iterate through query and map to user list
        for (Object result : doctorQuery) {
            Object[] doctorFields = (Object[]) result;
            User doctor = new User();
            doctor.setFirstName(doctorFields[0].toString());
            doctor.setSurName(doctorFields[1].toString());
            doctor.setPhone(doctorFields[2].toString());
            doctors.add(doctor);
        }

        return doctors;
    }

    /**
     * Create table and input data into it
     * @param data
     */
    private void createTable(List<User> data) {
        doctorTable.getColumns().clear();

        // Create and map first name column
        TableColumn<User, String> firstNameColumn = new TableColumn<>("First name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        // Create and map last name column
        TableColumn<User, String> lastNameColumn = new TableColumn<>("Last name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("surName"));

        // Create and map phone number column
        TableColumn<User, String> phoneColumn = new TableColumn<>("Phone number");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Add columns to table
        doctorTable.getColumns().add(firstNameColumn);
        doctorTable.getColumns().add(lastNameColumn);
        doctorTable.getColumns().add(phoneColumn);

        // Add data to table
        ObservableList<User> viewingData = FXCollections.observableArrayList(data);
        doctorTable.setItems(viewingData);
    }
}
