package com.comp5590.tests.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.comp5590.database.entities.Medicine;
import com.comp5590.database.entities.Prescription;
import com.comp5590.managers.ScreenManager;
import com.comp5590.screens.PrescriptionScreen;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionScreenTests extends ApplicationTest {

    private PrescriptionScreen prescriptionScreen;
    private VBox centerBox;

    @BeforeEach
    public void setUp() throws Exception {
        Stage stage = new Stage();
        ScreenManager screenManager = new ScreenManager(stage);
        prescriptionScreen = new PrescriptionScreen(screenManager);
        prescriptionScreen.setup();
        centerBox = getCenterBox(prescriptionScreen.getRootPane());
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Platform.exit();
    }

    private VBox getCenterBox(Parent rootPane) {
        return (VBox) ((BorderPane) rootPane).getCenter();
    }

    @Test
    public void testScreenSetup() {
        assertNotNull(prescriptionScreen.getRootPane());
        assertNotNull(centerBox);
        assertThat(centerBox).isInstanceOf(VBox.class);
    }

    @Test
    public void testPrescriptionCardCreation() {
        List<Prescription> prescriptions = new ArrayList<>();
        // add some examples if needed
        prescriptions.add(new Prescription());
        prescriptions.add(new Prescription());
        prescriptions.add(new Prescription());

        // displaying prescriptions
        prescriptionScreen.displayPrescriptions(prescriptions);

        List<Node> prescriptionCards = centerBox.getChildren();

        assertEquals(prescriptions.size(), prescriptionCards.size());

        for (Node card : prescriptionCards) {
            assertThat(card).isNotNull();
            assertThat(card).isInstanceOf(VBox.class);
        }
    }

    @Test
    public void testPrescriptionTableCreation() {
        Prescription prescription = new Prescription();
        List<Medicine> medicines = new ArrayList<>();
        medicines.add(new Medicine(0, "Medicine 1", 10, prescription));
        medicines.add(new Medicine(0, "Medicine 2", 20, prescription));
        prescription.setMedicine(medicines);

        TableView<Medicine> tableView = prescriptionScreen.createPrescriptionTable(prescription);

        assertNotNull(tableView);
        assertThat(tableView.getColumns()).hasSize(2); 
        assertThat(tableView.getColumns().get(0).getText()).isEqualTo("Medicine Name");
        assertThat(tableView.getColumns().get(1).getText()).isEqualTo("Dose");
        assertThat(tableView.getItems()).hasSize(medicines.size());
    }

}
