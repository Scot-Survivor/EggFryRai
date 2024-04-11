package com.comp5590.tests.integration;

import com.comp5590.App;
import com.comp5590.database.entities.Medicine;
import com.comp5590.database.entities.Prescription;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.screens.PrescriptionScreen;
import com.comp5590.tests.basic.SetupTests;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import static org.junit.jupiter.api.Assertions.assertEquals;
import javafx.scene.control.Label;
import java.util.List;

@ExtendWith(ApplicationExtension.class)
public class PrescriptionScreenTests extends SetupTests {

    private App app;

    @Start
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
        stage.show();
    }

    @Test
    public void testPrescriptionScreen(FxRobot robot) {
    // create a prescription 
    Prescription prescription = new Prescription();
    prescription.setAdditionalNotes("Test Prescription");
    DatabaseManager.getInstance().update(prescription);

    // create some medicines for the prescription
    Medicine medicine1 = new Medicine("medicine 1", 10, prescription);
    Medicine medicine2 = new Medicine("medicine 2", 20, prescription);
    DatabaseManager.getInstance().update(medicine1);
    DatabaseManager.getInstance().update(medicine2);

    goToScreen(app, robot, PrescriptionScreen.class);

    // check if prescription details are displayed correctly
    String prescriptionDetails = null;
    for (javafx.scene.Node node : robot.lookup("#prescriptionDetails").queryAll()) {
        if (node instanceof Label) {
            prescriptionDetails = ((Label) node).getText();
            break;
        }
    }
    assertEquals("Test Prescription", prescriptionDetails);

    // check if medicine details are displayed correctly
    String medicineName0 = null;
    String medicineDose0 = null;
    String medicineName1 = null;
    String medicineDose1 = null;
    List<javafx.scene.Node> medicineNodes = (List<javafx.scene.Node>) robot.lookup(".medicine-details").queryAll();
    if (medicineNodes.size() > 0) {
        medicineName0 = ((Label) medicineNodes.get(0)).getText();
        medicineDose0 = ((Label) medicineNodes.get(1)).getText();
    }
    if (medicineNodes.size() > 2) {
        medicineName1 = ((Label) medicineNodes.get(2)).getText();
        medicineDose1 = ((Label) medicineNodes.get(3)).getText();
    }
    assertEquals("medicine 1", medicineName0);
    assertEquals("10", medicineDose0);
    assertEquals("medicine 2", medicineName1);
    assertEquals("20", medicineDose1);
}

}

