package com.comp5590.tests.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.comp5590.App;
import com.comp5590.database.entities.User;
import com.comp5590.screens.DocListScreen;
import com.comp5590.security.managers.mfa.TOTPManager;
import com.comp5590.tests.basic.SetupTests;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class) // TestFX Extension)
public class DocListScreenTest extends SetupTests {

    App app;
    TOTPManager totpManager;

    @Start // This is similar to @BeforeAll it will run before all tests,
    // this is where we can get the stage and start the application
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
        app.getScreenManager().showScene(DocListScreen.class);
        totpManager = app.getTotpManager();
        stage.show();
    }

    private Pane getDocListScreen() {
        return (Pane) app.getScreenManager().getScreens().get(DocListScreen.class).getRoot();
    }

    /**
     * Test that the doctor list screen has a table
     */
    @Test
    public void testScreenHasDoctorTable(FxRobot robot) {
        robot.interact(() -> {
            // new test ends up in an async race condition, so we should sleep for a full
            // second to ensure it doesn't occur
            robot.sleep(1000); // Sleep for a second to ensure the screen is loaded
            Pane docListScreenPane = getDocListScreen();
            Set<Node> doctorTable = docListScreenPane.lookupAll("#doctorTable");
            assertThat(doctorTable).isNotNull();
        });
    }

    /**
     * Test that the doctor list screen has a button
     */
    @Test
    public void testScreenHasButton(FxRobot robot) {
        robot.interact(() -> {
            // new test ends up in an async race condition, so we should sleep for a full
            // second to ensure it doesn't occur
            robot.sleep(1000); // Sleep for a second to ensure the screen is loaded
            Pane docListScreenPane = getDocListScreen();
            Set<Node> switchButton = docListScreenPane.lookupAll("#switchButton");
            assertThat(switchButton).isNotNull();
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

        // Create DocListScreen instance and run fillTable on that instance
        DocListScreen docListScreenInstance = (DocListScreen) app
            .getScreenManager()
            .getScreenInstance(DocListScreen.class);
        Platform.runLater(docListScreenInstance::fillTable);

        WaitForAsyncUtils.waitForFxEvents(); // Wait for fillTable before proceeding

        // Check rows of table
        ObservableList<User> rows = docListScreenInstance.getDoctorTable().getItems();
        assertThat(rows).isNotNull();
        assertThat(rows.size()).isEqualTo(2);
    }

    /**
     * Test to check if warning box pops up when trying to select with no table selection
     * @param robot Will be injected via test runner
     */
    @Test
    public void testChangeDoctorButtonNoSelect(FxRobot robot) {
        robot.interact(() -> {
            robot.clickOn("#switchButton");
        });
        FxAssert.verifyThat("#noSelect", NodeMatchers.isVisible());
    }

    /**
     * Test to check if warning box pops up when trying to select with no table selection
     * @param robot Will be injected via test runner
     */
    @Test
    public void testChangeDoctorButtonSelect(FxRobot robot) {
        // Create row on table
        createDoctor("email1@example.com", "pa321321");
        DocListScreen docListScreenInstance = (DocListScreen) app
            .getScreenManager()
            .getScreenInstance(DocListScreen.class);
        Platform.runLater(docListScreenInstance::fillTable);

        // Select row and click button
        TableView doctorTable = robot.lookup("#doctorTable").queryAs(TableView.class);
        robot.interact(() -> {
            doctorTable.getSelectionModel().selectFirst();
            robot.clickOn("#switchButton");
        });
        Node doctorSelectedNode = robot.lookup("#doctorSelected").query();
        assertTrue(doctorSelectedNode.isVisible());
    }
}
