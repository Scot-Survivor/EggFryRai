package com.comp5590.screens;

import com.comp5590.entities.User;
import com.comp5590.managers.ScreenManager;
import javafx.event.ActionEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.control.*;

public class MFAScreen extends AbstractScreen{
    private TextField code;
    private Label error;

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
            getApp().getScreenManager().showScene(HomeScreen.class);
            getApp().setCurrentUser(user);
        } else {
            LoginScreen loginScreen = (LoginScreen)
                    getApp().getScreenManager().getScreenInstances().get(LoginScreen.class);
            loginScreen.setErrorText("Invalid 2FA code");
            getApp().getScreenManager().showScene(LoginScreen.class);
            getApp().setCurrentUser(null); // clear the current user
        }
    }

    private boolean verify(String code, User user) {
        return getApp().getTotpManager().verifyRecoveryCode(user.getAuthenticationDetails().getRecoveryCodes(), code) ||
                getApp().getTotpManager().verifyCode(user.getAuthenticationDetails().getAuthenticationToken(), code);
    }

    private HBox createTitle() {
        Text text = new Text("Enter your 2FA below");
        return new HBox(text);
    }
}
