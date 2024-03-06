package com.comp5590.screens;

import com.comp5590.App;
import com.comp5590.entities.Patient;
import com.comp5590.managers.DatabaseManager;
import com.comp5590.managers.ScreenManager;
import javafx.event.ActionEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import org.hibernate.SessionFactory;

public class MFAScreen extends AbstractScreen{
    private SessionFactory sessionFactory;
    private App app;
    private TextField code;
    private Label error;

    public MFAScreen(ScreenManager screenManager) {
        super(screenManager);
        this.sessionFactory = DatabaseManager.getInstance().getSessionFactory();
        this.app = App.getInstance();
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
        Patient user = app.getCurrentUser();
        boolean valid = this.verify(code, user);
        if (valid) {
            app.getScreenManager().showScene(HomeScreen.class);
            app.setCurrentUser(user);
        } else {
            LoginScreen loginScreen = (LoginScreen)
                    app.getScreenManager().getScreenInstances().get(LoginScreen.class);
            loginScreen.setErrorText("Invalid 2FA code");
            app.getScreenManager().showScene(LoginScreen.class);
            app.setCurrentUser(null); // clear the current user
        }
    }

    private boolean verify(String code, Patient user) {
        return app.getTotpManager().verifyRecoveryCode(user.getRecoveryCodes(), code) ||
                app.getTotpManager().verifyCode(user.getAuthenticationToken(), code);
    }

    private HBox createTitle() {
        Text text = new Text("Enter your 2FA below");
        return new HBox(text);
    }
}
