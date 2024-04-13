package com.comp5590.tests.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.comp5590.App;
import com.comp5590.database.entities.User;
import com.comp5590.screens.ChooseDoctorScreen;
import com.comp5590.security.managers.mfa.TOTPManager;
import com.comp5590.tests.basic.SetupTests;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.service.query.NodeQuery;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class) // TestFX Extension)
public class ChooseDoctorScreenTest extends SetupTests {

    App app;
    TOTPManager totpManager;

    @Start // This is similar to @BeforeAll it will run before all tests,
    // this is where we can get the stage and start the application
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
        totpManager = app.getTotpManager();
        stage.show();
    }

    /**
     * Test that the choose doctor screen has a table
     */
    @Test
    public void testScreenHasDoctorTable(FxRobot robot) {
        robot.interact(() -> {
            // new test ends up in an async race condition, so we should sleep for a full
            // second to ensure it doesn't occur
            robot.sleep(100); // Sleep for a second to ensure the screen is loaded
            NodeQuery doctorTable = robot.lookup("#doctorTable");
            assertThat(doctorTable.queryAll()).isNotNull();
        });
    }

    /**
     * Test that the choose doctor screen has a button
     */
    @Test
    public void testScreenHasButton(FxRobot robot) {
        robot.interact(() -> {
            // new test ends up in an async race condition, so we should sleep for a full
            // second to ensure it doesn't occur
            robot.sleep(100); // Sleep for a second to ensure the screen is loaded
            NodeQuery switchButton = robot.lookup("#switchButton");
            assertThat(switchButton.queryAll()).isNotNull();
        });
    }

    /**
     * Test that the table is populated with correct users
     */
    @Test
    public void testPopulatedTable() {
        // Setup for doctor 1
        createDoctor("email1@example.com", "pa321321");

        // Setup for doctor 2
        createDoctor("email2@example.com", "pa321321");

        // Create patient 1 (saved to DB in method)
        createPatient("email3@example.com", "pa42131");

        // Create ChooseDoctorScreen instance and run fillTable on that instance
        ChooseDoctorScreen ChooseDoctorScreenInstance = app
            .getScreenManager()
            .getScreenInstance(ChooseDoctorScreen.class);
        Platform.runLater(ChooseDoctorScreenInstance::fillTable);

        WaitForAsyncUtils.waitForFxEvents(); // Wait for fillTable before proceeding

        // Check rows of table
        ObservableList<User> rows = ChooseDoctorScreenInstance.getDoctorTable().getItems();
        assertThat(rows).isNotNull();
        assertThat(rows.size()).isEqualTo(2);
    }

    /**
     * Test to check if warning label pops up when trying to select with no table selection
     * @param robot Will be injected via test runner
     */
    @Test
    public void testChangeDoctorButtonNoSelect(FxRobot robot) {
        goToScreenWithAutoAuthentication(app, robot, ChooseDoctorScreen.class);

        robot.interact(() -> {
            robot.lookup("#switchButton").queryAs(javafx.scene.control.Button.class).fire();

            // Check correct label is showing
            assertThat(robot.lookup("#resultLabel").queryAs(javafx.scene.control.Label.class).getText())
                .isEqualTo("You must select a doctor to change to.");
        });
    }

    /**
     * Test to check if success label pops up when trying to select with a table selection
     * @param robot Will be injected via test runner
     */
    @Test
    public void testChangeDoctorButtonSelect(FxRobot robot) {
        goToScreenWithAutoAuthentication(app, robot, ChooseDoctorScreen.class);

        // Create row on table
        createDoctor("email1@example.com", "pa321321");

        // Run fillTable on screen
        ChooseDoctorScreen ChooseDoctorScreenInstance = app
            .getScreenManager()
            .getScreenInstance(ChooseDoctorScreen.class);
        Platform.runLater(ChooseDoctorScreenInstance::fillTable);

        // Select row and click button
        TableView doctorTable = robot.lookup("#doctorTable").queryAs(TableView.class);
        robot.interact(() -> {
            doctorTable.getSelectionModel().selectFirst();
            robot.lookup("#switchButton").queryAs(javafx.scene.control.Button.class).fire();
        });

        // See if label is accurate (with firstname/surname being Test/User)
        assertThat(robot.lookup("#resultLabel").queryAs(javafx.scene.control.Label.class).getText())
            .isEqualTo("You have switched your doctor to Test User.");
    }
}
