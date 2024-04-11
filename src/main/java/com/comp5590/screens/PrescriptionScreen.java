package com.comp5590.screens;

import com.comp5590.database.entities.Medicine;
import com.comp5590.database.entities.Prescription;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.managers.ScreenManager;
import com.comp5590.security.managers.authentication.annotations.AuthRequired;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;

@AuthRequired
public class PrescriptionScreen extends AbstractScreen {

    public PrescriptionScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        this.addCss("/prescription.css");

        BorderPane rootPane = new BorderPane();
        setRootPane(rootPane);
        
        VBox centeringContainer = new VBox();
        centeringContainer.setAlignment(Pos.CENTER_RIGHT);
        
        centeringContainer.getChildren().add(createCenter());
        rootPane.setCenter(centeringContainer);

        addBackAndHomeButtons(rootPane);
    }


    private VBox createCenter() {
        VBox centerBox = new VBox();
        centerBox.getStyleClass().add("center-box");

        DatabaseManager dbManager = getDatabaseManager();
        List<Prescription> prescriptions = dbManager.getAll(Prescription.class);

        if (prescriptions.isEmpty()) {
            centerBox.getChildren().add(new Label("No prescriptions available."));
        } else {
            for (Prescription prescription : prescriptions) {
                centerBox.getChildren().add(createPrescriptionCard(prescription));
            }
        }

        return centerBox;
    }

    private TableView<Medicine> createPrescriptionTable(Prescription prescription) {
        TableView<Medicine> tableView = new TableView<>();

        //columns for medicine name and dose
        TableColumn<Medicine, String> medicineNameColumn = new TableColumn<>("Medicine Name");
        medicineNameColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));

        TableColumn<Medicine, Integer> doseColumn = new TableColumn<>("Dose");
        doseColumn.setCellValueFactory(new PropertyValueFactory<>("recomendedDose"));

        //columns to the table
        tableView.getColumns().addAll(medicineNameColumn, doseColumn);

        //medicines associated with the prescription to the table
        tableView.getItems().addAll(prescription.getMedicine());

        return tableView;
    }

    private VBox createPrescriptionCard(Prescription prescription) {
        VBox prescriptionCard = new VBox();
        prescriptionCard.getStyleClass().add("prescription-card");

        // display prescription details
        Label dateLabel = new Label("Date: " + prescription.getDateOfPrescription().toString());

        // create and add prescription table
        TableView<Medicine> prescriptionTable = createPrescriptionTable(prescription);
        prescriptionCard.getChildren().addAll(dateLabel, prescriptionTable);

        return prescriptionCard;
    }

    @Override
    public void cleanup() {
    }
}
