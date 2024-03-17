package com.comp5590.screens;

import com.comp5590.database.entities.User;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.apache.logging.log4j.core.Logger;

public class MFAScreen extends AbstractScreen {

    private TextField code;
    private Label error;

    private final Logger logger = LoggerManager.getInstance().getLogger(LoginScreen.class);

    public MFAScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        BorderPane pane = new BorderPane();
        pane.setCenter(create2FA());
        pane.setTop(createTitle());
        setRootPane(pane);
    }

    private HBox create2FA() {
        Label codeLabel = new Label("2FA Code:");
        this.code = new TextField();
        this.code.setId("code");

        this.error = new Label();

        Button submit = new Button("Submit");
        submit.setId("submit");
        submit.setOnAction(this::submitCode);

        return new HBox(codeLabel, this.code, submit, this.error);
    }

    private void submitCode(ActionEvent e) {
        String code = this.code.getText();
        User user = getApp().getCurrentUser();
        boolean valid = this.verify(code, user);
        if (valid) {
            getApp().setCurrentUser(user);
            // set the user as authenticated in session manager
            getApp().getSessionManager().setAuthenticated(true);
            // unauthenticate user after 2 hours, forcing them to re-login
            getApp().getSessionManager().unauthenticateAfter(2);
            // show the home screen
            getApp().getScreenManager().showScene(HomeScreen.class);
            logger.info("User is logged in successfully. as {}", user.getAuthenticationDetails().getEmail());
        } else {
            LoginScreen loginScreen = (LoginScreen) getApp().getScreenManager().getScreenInstance(LoginScreen.class);
            loginScreen.setErrorText("Invalid 2FA code");
            getApp().getScreenManager().showScene(LoginScreen.class);
            getApp().setCurrentUser(null); // clear the current user
        }
    }

    private boolean verify(String code, User user) {
        return (
            getApp().getTotpManager().verifyRecoveryCode(user.getAuthenticationDetails().getRecoveryCodes(), code) ||
            getApp().getTotpManager().verifyCode(user.getAuthenticationDetails().getAuthenticationToken(), code)
        );
    }

    private HBox createTitle() {
        Text text = new Text("Enter your 2FA below");
        return new HBox(text);
    }
}
