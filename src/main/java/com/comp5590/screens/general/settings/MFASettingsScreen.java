package com.comp5590.screens.general.settings;

import com.comp5590.configuration.AppConfig;
import com.comp5590.database.entities.AuthenticationDetails;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.events.enums.UserAttribute;
import com.comp5590.events.eventtypes.users.UserUpdateEvent;
import com.comp5590.events.managers.EventManager;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.managers.ScreenManager;
import com.comp5590.screens.misc.AbstractScreen;
import com.comp5590.security.authentication.annotations.AuthRequired;
import com.comp5590.security.managers.mfa.TOTPManager;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.core.Logger;

@AuthRequired
public class MFASettingsScreen extends AbstractScreen {

    private final TOTPManager totpManager;
    private final Logger logger;
    private final SessionManager sessionManager;
    private final DatabaseManager databaseManager;
    private TextField codeInput;
    private String secret;
    private String recoveryCodes;
    private Label resultLabel;

    private boolean showRecoveryCodes = false;

    public MFASettingsScreen(ScreenManager screenManager) {
        super(screenManager);
        this.totpManager = TOTPManager.getInstance();
        this.logger = LoggerManager.getInstance().getLogger(MFASettingsScreen.class);
        this.sessionManager = SessionManager.getInstance();
        this.databaseManager = DatabaseManager.getInstance();
    }

    @Override
    public void setup() {
        this.addCss("/css/mfaSettings.css");
        secret = totpManager.generateSecret();
        GridPane pane = attachDefaultPane();
        attachHeaderAndNavBar("Your Profile - 2FA");

        ((BorderPane) getRootPane()).setCenter(center());
    }

    private VBox center() {
        VBox content = new VBox();
        if (!showRecoveryCodes) {
            VBox mfaForm = enable2FAForm();
            mfaForm.setAlignment(Pos.TOP_CENTER);
            content.getChildren().add(mfaForm);
        } else {
            VBox recoveryCodesForm = getRecoveryCodesForm();
            recoveryCodesForm.setAlignment(Pos.TOP_CENTER);
            content.getChildren().add(recoveryCodesForm);
        }

        resultLabel = new Label();
        resultLabel.setAlignment(Pos.TOP_CENTER);
        content.getChildren().add(resultLabel);
        content.setAlignment(Pos.TOP_CENTER);
        return content;
    }

    private VBox getRecoveryCodesForm() {
        VBox formBox = new VBox();
        String[] recoveryCodesArray = recoveryCodes.split(",");
        // Display each recovery code in scrollable list
        for (String code : recoveryCodesArray) {
            TextField codeLabel = new TextField(code);
            codeLabel.getStyleClass().add("copyable-label");
            codeLabel.setAlignment(Pos.TOP_CENTER);
            codeLabel.setPadding(new Insets(5));
            formBox.getChildren().add(codeLabel);
        }
        Button confirm = new Button("I have saved my recovery codes");
        confirm.setOnAction(e -> {
            showRecoveryCodes = false;

            User currentUser = sessionManager.getCurrentUser();
            AuthenticationDetails authDetails = currentUser.getAuthenticationDetails();
            authDetails.setRecoveryCodes(recoveryCodes);
            currentUser.setAuthenticationDetails(authDetails);

            if (databaseManager.update(authDetails) && databaseManager.update(currentUser)) {
                resultLabel.setText("Recovery codes saved successfully");
                resultLabel.setStyle("-fx-text-fill: green;");
                logger.info(this.sessionManager.getFullName() + " saved recovery codes");
                EventManager
                    .getInstance()
                    .callEvent(new UserUpdateEvent(UserAttribute.MFAs, "REDACTED", currentUser.getId()));
                secret = null;
                recoveryCodes = null;
                getScreenManager().goBack();
            } else {
                resultLabel.setText("Failed to save recovery codes. Please regenerate your 2FA settings");
                resultLabel.setStyle("-fx-text-fill: red;");
                logger.warn(this.sessionManager.getFullName() + " failed to save recovery codes");
            }
        });
        confirm.setAlignment(Pos.TOP_CENTER);
        formBox.getChildren().add(confirm);
        return formBox;
    }

    private VBox enable2FAForm() {
        VBox formBox = new VBox();

        byte[] pngImageData = totpManager.generatePngImageData(secret);

        // Use swing utils to convert pngImageData to javaFX image
        InputStream iStream = new ByteArrayInputStream(pngImageData);
        Image image = new Image(iStream);

        // Create a new image view
        ImageView imageView = new ImageView(image);

        // Create a new label
        TextField secretLabel;
        if (AppConfig.TOTP_ALGORITHM.equals("SHA1")) {
            secretLabel = new TextField("Secret: " + secret);
            secretLabel.setEditable(false);
            secretLabel.getStyleClass().add("copyable-label");
            secretLabel.setAlignment(Pos.TOP_CENTER);
        } else {
            secretLabel =
            new TextField(
                "This App doesn't not support raw secret for " +
                AppConfig.TOTP_ALGORITHM +
                " please ask your administrator to change the algorithm to SHA1"
            );
            secretLabel.setEditable(false);
            secretLabel.getStyleClass().add("copyable-label");
            secretLabel.setAlignment(Pos.TOP_CENTER);
        }

        formBox.getChildren().addAll(imageView, secretLabel);

        HBox confirmationComponents = new HBox();
        // We also need to confirm that they've done it correctly so create input box
        Label confirmLabel = new Label("Enter the code from your 2FA app:");
        confirmationComponents.getChildren().add(confirmLabel);
        codeInput = new TextField();
        confirmationComponents.getChildren().add(codeInput);
        confirmationComponents.setAlignment(Pos.TOP_CENTER);

        formBox.getChildren().add(confirmationComponents);

        Button confirm = new Button("Submit");
        confirm.setOnAction(this::approve2FA);
        formBox.getChildren().add(confirm);

        return formBox;
    }

    public void approve2FA(ActionEvent e) {
        boolean isValid = false;
        if (totpManager.verifyCode(secret, codeInput.getText())) {
            recoveryCodes = totpManager.generateRecoveryCodes();
            User currentUser = sessionManager.getCurrentUser();
            AuthenticationDetails authDetails = currentUser.getAuthenticationDetails();
            authDetails.setTwoFactorEnabled(true);
            authDetails.setAuthenticationToken(secret);
            authDetails.setRecoveryCodes(recoveryCodes);
            currentUser.setAuthenticationDetails(authDetails);

            if (databaseManager.update(authDetails) && databaseManager.update(currentUser)) {
                isValid = true;
            }
        }

        if (isValid) {
            resultLabel.setText("2FA enabled successfully");
            resultLabel.setStyle("-fx-text-fill: green;");
            logger.info(this.sessionManager.getFullName() + " enabled 2FA");
            showRecoveryCodes = true;
            this.refreshScene();
        } else {
            resultLabel.setText("Invalid 2FA code");
            resultLabel.setStyle("-fx-text-fill: red;");
            logger.warn(this.sessionManager.getFullName() + " failed to enable 2FA");
        }
    }

    @Override
    public void cleanup() {}
}
