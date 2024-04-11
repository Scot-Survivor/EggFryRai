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
        centeringContainer.setAlignment(Pos.CENTER);

        DatabaseManager dbManager = getDatabaseManager();
        List<Prescription> prescriptions = dbManager.getAll(Prescription.class);

        VBox centerBox = new VBox();
        centerBox.getStyleClass().add("center-box");

        if (prescriptions.isEmpty()) {
            centerBox.getChildren().add(new Label("No prescriptions available."));
        } else {
            for (Prescription prescription : prescriptions) {
                centerBox.getChildren().add(createPrescriptionCard(prescription));
            }
        }

        centeringContainer.getChildren().add(centerBox);
        rootPane.setCenter(centeringContainer);

        addBackAndHomeButtons(rootPane);
    }

    public TableView<Medicine> createPrescriptionTable(Prescription prescription) {
        TableView<Medicine> tableView = new TableView<>();
        // Columns for medicine name and dose
        TableColumn<Medicine, String> medicineNameColumn = new TableColumn<>("Medicine Name");
        medicineNameColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));

        TableColumn<Medicine, Integer> doseColumn = new TableColumn<>("Dose");
        doseColumn.setCellValueFactory(new PropertyValueFactory<>("recommendedDose"));

        // Add columns to the table
        tableView.getColumns().addAll(medicineNameColumn, doseColumn);

        // Add medicines associated with the prescription to the table
        tableView.getItems().addAll(prescription.getMedicine());

        return tableView;
    }

    private VBox createPrescriptionCard(Prescription prescription) {
        VBox prescriptionCard = new VBox();
        prescriptionCard.getStyleClass().add("prescription-card");

        // Display prescription details
        String dateText = prescription.getDateOfPrescription() != null ? prescription.getDateOfPrescription().toString() : "Unknown Date";
        Label dateLabel = new Label("Date: " + dateText);

        // Create prescription table
        TableView<Medicine> prescriptionTable = createPrescriptionTable(prescription);
        prescriptionCard.getChildren().addAll(dateLabel, prescriptionTable);

        // Generate prescription details string
        StringBuilder prescriptionDetails = new StringBuilder();
        prescriptionDetails.append("Date: ").append(dateText).append("\n");
        String additionalNotes = prescription.getAdditionalNotes() != null ? prescription.getAdditionalNotes() : "No additional notes";
        prescriptionDetails.append("Additional Notes: ").append(additionalNotes).append("\n");

        // Print prescription details for debugging
        System.out.println("Prescription Details: " + prescriptionDetails.toString());

        return prescriptionCard;
    }

    public void displayPrescriptions(List<Prescription> prescriptions) {
        VBox centerBox = (VBox) ((BorderPane) getRootPane()).getCenter();
        centerBox.getChildren().clear(); // Clear existing content
    
        for (Prescription prescription : prescriptions) {
            centerBox.getChildren().add(createPrescriptionCard(prescription));
        }
    }
    

    @Override
    public void cleanup() {
    }

}
