package com.comp5590.tests.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.comp5590.App;
import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.AuthenticationDetails;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.enums.CommunicationPreference;
import com.comp5590.enums.UserRole;
import com.comp5590.screens.DocListScreen;
import com.comp5590.security.managers.mfa.TOTPManager;
import com.comp5590.tests.basic.SetupTests;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
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
     * Test that the table is populated
     */
    @Test
    public void testPopulatedTable() {
        // Make address for doctors
        Address address = createAddress();

        // Setup for user 1
        AuthenticationDetails authenticationDetails1 = createAuthenticationDetails("email4@example.com", "pa23121");
        User testUser = new User();
        testUser.setFirstName("ta");
        testUser.setSurName("bd");
        testUser.setPhone("999");
        testUser.setAddress(address);
        testUser.setFax("3211");
        testUser.setAdditionalNotes("note");
        testUser.setCommunicationPreference(CommunicationPreference.NONE);
        testUser.setRole(UserRole.DOCTOR);
        testUser.setAuthenticationDetails(authenticationDetails1);

        // Setup for user 2
        AuthenticationDetails authenticationDetails2 = createAuthenticationDetails("email1@example.com", "pa23121");
        User testUser2 = new User();
        testUser2.setFirstName("ata");
        testUser2.setSurName("bdd");
        testUser2.setPhone("998");
        testUser2.setAddress(address);
        testUser2.setFax("3211");
        testUser2.setAdditionalNotes("note");
        testUser2.setCommunicationPreference(CommunicationPreference.NONE);
        testUser2.setRole(UserRole.DOCTOR);
        testUser2.setAuthenticationDetails(authenticationDetails2);

        // Save users to database
        DatabaseManager db = DatabaseManager.getInstance();
        boolean saveResult1 = db.save(testUser);
        boolean saveResult2 = db.save(testUser2);

        // Check that users saved correctly
        assertTrue(saveResult1);
        assertTrue(saveResult2);

        // Create DocListScreen instance and run fillTable on that instance
        DocListScreen docListScreenInstance = (DocListScreen) app
            .getScreenManager()
            .getScreenInstance(DocListScreen.class);
        Platform.runLater(docListScreenInstance::fillTable);

        WaitForAsyncUtils.waitForFxEvents(); // Wait for fillTable before proceeding

        // Check rows of table
        ObservableList<User> items = docListScreenInstance.getDoctorTable().getItems();
        assertThat(items).isNotNull();
        assertThat(items.size()).isEqualTo(2);
    }
}
