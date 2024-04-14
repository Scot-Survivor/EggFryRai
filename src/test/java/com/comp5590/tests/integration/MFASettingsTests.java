package com.comp5590.tests.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.comp5590.App;
import com.comp5590.database.entities.User;
import com.comp5590.screens.general.settings.MFASettingsScreen;
import com.comp5590.security.managers.mfa.TOTPManager;
import com.comp5590.tests.basic.SetupTests;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class MFASettingsTests extends SetupTests {

    private App app;
    private TOTPManager totpManager;

    private String testSecret = "123456";

    private byte[] qrCodeData;

    private String recoveryCodes;

    @Start
    public void start(Stage stage) {
        qrCodeData = TOTPManager.getInstance().generatePngImageData(testSecret);
        recoveryCodes = TOTPManager.getInstance().generateRecoveryCodes();
        totpManager = mock(TOTPManager.class);
        setMock(totpManager);
        when(totpManager.generateSecret()).thenReturn(testSecret);
        when(totpManager.generatePngImageData(testSecret)).thenReturn(qrCodeData);
        when(totpManager.generateRecoveryCodes()).thenReturn(recoveryCodes);
        when(totpManager.verifyCode(testSecret, "654321")).thenReturn(false);
        when(totpManager.verifyCode(testSecret, "123456")).thenReturn(true);
        app = new App();
        app.start(stage);
        stage.show();
    }

    private void setMock(TOTPManager mock) {
        try {
            Field instance = TOTPManager.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void resetSingleton() throws Exception {
        Field instance = TOTPManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void test2FACreationFailsOnInvalidCode(FxRobot robot) {
        User user = createPatient("example@example.com", "password");
        goToScreenWithAuthentication(app, robot, MFASettingsScreen.class, "example@example.com", "password");

        assertEquals(user.getId(), app.getSessionManager().getCurrentUser().getId());

        robot.interact(() -> {
            assertNotNull(robot.lookup("#mfa-form").query());
            AtomicReference<String> secret = new AtomicReference<>();
            robot
                .lookup(".copyable-label")
                .queryAllAs(TextField.class)
                .stream()
                .findFirst()
                .ifPresent(textField -> {
                    secret.set(textField.getText());
                });
            assertNotNull(secret.get());
            assertNotEquals(0, secret.get().length());

            robot.lookup("#codeInput").queryAs(TextField.class).setText("654321");

            robot.lookup("#confirm").queryAs(Button.class).fire();
            // We should still be on the same screen
            assertInstanceOf(MFASettingsScreen.class, app.getScreenManager().getCurrentScreen());
            stall(robot, 1000); // Delay is hard coded, this is to allow the screen to change
            String text =
                ((MFASettingsScreen) app.getScreenManager().getScreenInstance(MFASettingsScreen.class)).getResultLabel()
                    .getText();
            assertNotEquals(0, text.length());
        });
    }

    @Test
    public void test2FACreationSucceedsOnValidCode(FxRobot robot) {
        User user = createPatient("example@example.com", "password");
        goToScreenWithAuthentication(app, robot, MFASettingsScreen.class, "example@example.com", "password");

        assertEquals(user.getId(), app.getSessionManager().getCurrentUser().getId());

        robot.interact(() -> {
            assertNotNull(robot.lookup("#mfa-form").query());
            AtomicReference<String> secret = new AtomicReference<>();
            robot
                .lookup(".copyable-label")
                .queryAllAs(TextField.class)
                .stream()
                .findFirst()
                .ifPresent(textField -> {
                    secret.set(textField.getText());
                });
            assertNotNull(secret.get());
            assertNotEquals(0, secret.get().length());

            when(totpManager.verifyCode(secret.get(), "654321")).thenReturn(true);

            robot.lookup("#codeInput").queryAs(TextField.class).setText("123456");

            robot.lookup("#confirm").queryAs(Button.class).fire();

            // We should still be on the same screen
            assertInstanceOf(MFASettingsScreen.class, app.getScreenManager().getCurrentScreen());

            assertTrue(
                ((MFASettingsScreen) app
                        .getScreenManager()
                        .getScreenInstance(MFASettingsScreen.class)).isShowRecoveryCodes()
            );
            stall(robot, 2000); // Refresh is expensive, give it a bit of time
            robot.lookup("#confirm").queryAs(Button.class).fire();
            stall(robot, 2000); // Delay is hard coded, this is to allow the screen to change

            assertEquals(app.getSessionManager().getStateMessage(), "Successfully enabled 2FA.");
        });
    }
}
