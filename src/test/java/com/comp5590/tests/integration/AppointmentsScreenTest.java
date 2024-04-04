package com.comp5590.tests.integration;

import com.comp5590.App;
import com.comp5590.screens.AppointmentsScreen;
import com.comp5590.tests.basic.SetupTests;
import com.comp5590.utils.EventUtils;
import com.comp5590.utils.QueryUtils;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class AppointmentsScreenTest extends SetupTests {

    private App app;

    @Start
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
        stage.show();
    }

    @Test
    public void testViewAppointmentsButton(FxRobot robot) {
        // go to appointments screen
        goToScreen(app, robot, AppointmentsScreen.class);

        // simulate clicking on the "View Appointments" button
        robot.interact(() -> {
            robot
                .lookup("#viewAppointmentsButton")
                .queryAs(QueryUtils.getButtonClass())
                .fireEvent(
                    EventUtils.createCustomMouseEvent(
                        MouseEvent.MOUSE_CLICKED,
                        0,
                        0,
                        0,
                        0,
                        MouseButton.PRIMARY,
                        1,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        null
                    )
                );
        });
    }

    @Test
    public void testEnterNewAppointmentButton(FxRobot robot) {
        // go to appointments screen
        goToScreen(app, robot, AppointmentsScreen.class);

        // simulate clicking on the "Enter New Appointment" button
        robot.interact(() -> {
            robot
                .lookup("#enterNewAppointmentButton")
                .queryAs(QueryUtils.getButtonClass())
                .fireEvent(
                    EventUtils.createCustomMouseEvent(
                        MouseEvent.MOUSE_CLICKED,
                        0,
                        0,
                        0,
                        0,
                        MouseButton.PRIMARY,
                        1,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        null
                    )
                );
        });

    }

    @Test
    public void testChangeAppointmentButton(FxRobot robot) {
        // go to appointments screen
        goToScreen(app, robot, AppointmentsScreen.class);

        // simulate clicking on the "Change Appointment" button
        robot.interact(() -> {
            robot
                .lookup("#changeAppointmentButton")
                .queryAs(QueryUtils.getButtonClass())
                .fireEvent(
                    EventUtils.createCustomMouseEvent(
                        MouseEvent.MOUSE_CLICKED,
                        0,
                        0,
                        0,
                        0,
                        MouseButton.PRIMARY,
                        1,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        null
                    )
                );
        });
    }
}
