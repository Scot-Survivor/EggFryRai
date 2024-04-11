package com.comp5590.screens;

import com.comp5590.database.entities.Medicine;
import com.comp5590.database.entities.Prescription;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.managers.ScreenManager;
import com.comp5590.security.managers.authentication.annotations.AuthRequired;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

@AuthRequired
public class PrescriptionScreen extends AbstractScreen {

    private TableView<Prescription> prescriptionTableView;

    public PrescriptionScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        GridPane gridPane = attachDefaultPane();
        attachHeaderAndNavBar("Prescription");

        // create prescription table
        prescriptionTableView = createPrescriptionTableView();
        gridPane.add(prescriptionTableView, 0, 1);
    }


    private TableView<Prescription> createPrescriptionTableView() {
        TableView<Prescription> tableView = new TableView<>();
        
        // define columns
        TableColumn<Prescription, String> medicineNameColumn = new TableColumn<>("Medicine Name");
        TableColumn<Prescription, String> dosageColumn = new TableColumn<>("Dosage");
        TableColumn<Prescription, String> notesColumn = new TableColumn<>("Additional Notes");
        
        // set cell value
        medicineNameColumn.setCellValueFactory(data -> {
            List<Medicine> medicines = data.getValue().getMedicine();
            if (!medicines.isEmpty()) {
                return new SimpleStringProperty(medicines.get(0).getMedicineName());
            } else {
                return new SimpleStringProperty("");
            }
        });
        dosageColumn.setCellValueFactory(data -> {
            List<Medicine> medicines = data.getValue().getMedicine();
            if (!medicines.isEmpty()) {
                return new SimpleStringProperty(String.valueOf(medicines.get(0).getRecomendedDose()));
            } else {
                return new SimpleStringProperty("");
            }
        });
        notesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAdditionalNotes()));
        
        // add columns to table
        tableView.getColumns().addAll(medicineNameColumn, dosageColumn, notesColumn);
        
        // load data into table
        DatabaseManager db = getDatabaseManager();
        try {
            List<Prescription> prescriptions = db.getAll(Prescription.class);
            tableView.getItems().addAll(prescriptions);
        } catch (Exception e) {
            e.printStackTrace(); // log the exception
        }
        
        return tableView;
    }
    
    
    @Override
    public void cleanup() {
    }

    @Override
    public boolean canAccess() {
        return true;
    }

    @Override
    public boolean canAccess(User user) {

        return true;
    }
    
}
