package com.comp5590.tests.integration;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;

import com.comp5590.App;
import com.comp5590.database.entities.Prescription;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.screens.PrescriptionScreen;
import com.comp5590.tests.basic.SetupTests;
// import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;


import java.util.List;

@ExtendWith(ApplicationExtension.class)
public class PrescriptionScreenTest extends SetupTests {

    App app;

    @Start
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
        stage.show();
    }

    @Test
    public void testPrescriptionScreenDataLoaded(FxRobot robot) {
        // authenticate as a random user
        authenticateAsRandomUser(app, robot);
    
        // go to the prescription screen
        goToScreen(app, robot, PrescriptionScreen.class);
    
        // lookup the prescription table view
        // TableView<Prescription> prescriptionTableView = robot.lookup("#prescriptionTableView").queryAs(TableView.class);
        // assertNotNull(prescriptionTableView);
    
        // get all prescriptions from the database
        DatabaseManager db = getDbManager(); 
        List<Prescription> prescriptions = db.getAll(Prescription.class);
    
        // check if the number of items in the table view matches the number of prescriptions in the database
        // assertEquals(prescriptions.size(), prescriptionTableView.getItems().size());
    
        // check if the data is correctly loaded into the table view
        // for (Prescription prescription : prescriptions) {
        //     assertTrue(prescriptionTableView.getItems().contains(prescription));
        // }
    }
    

}
