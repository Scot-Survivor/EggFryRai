package com.comp5590.screens.general.settings;

import com.comp5590.components.global.BigButton;
import com.comp5590.components.global.LoginField;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
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

    @Getter
    private Label resultLabel;

    @Getter
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
        attachHeaderAndNavBar(2, "Your Profile - 2FA");

        // make content
        VBox center = center();

        // add content to center of screen
        pane.add(center, 0, 1);
        center.setId("center");
        pane.setAlignment(Pos.CENTER);
    }

    private VBox center() {
        VBox content = new VBox();
        if (!sessionManager.has2FAEnabled() || showRecoveryCodes) {
            if (!showRecoveryCodes) {
                VBox mfaForm = enable2FAForm();
                mfaForm.setAlignment(Pos.TOP_CENTER);
                content.getChildren().add(mfaForm);
            } else {
                VBox recoveryCodesForm = getRecoveryCodesForm();
                recoveryCodesForm.setId("recoveryCodesForm");
                recoveryCodesForm.setAlignment(Pos.TOP_CENTER);
                content.getChildren().add(recoveryCodesForm);
            }
        } else {
            // create a VBox for storing children
            VBox disable2faBox = new VBox();

            // create components
            Label disableLabel = new Label("Are you sure you want to disable 2FA?");
            BigButton disableButton = new BigButton("Disable 2FA");
            // set listener for button
            disableButton.setOnAction(this::disable2FA);

            // add components to disable2fa
            disable2faBox.getChildren().addAll(disableLabel, disableButton);

            // set styles for all components
            disable2faBox.setId("disable2faBox");
            disableLabel.setId("disableLabel");
            disableButton.getStyleClass().add("big-button");

            // add disable2fa component to main screen content box
            content.getChildren().add(disable2faBox);
        }

        resultLabel = new Label();
        resultLabel.setId("resultLabel");
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
        confirm.setId("confirm");
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
                this.showSceneBetweenScenesThenNextScene("Successfully enabled 2FA.", ProfileScreen.class, 1000);
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
        formBox.setId("mfa-form");

        byte[] pngImageData = totpManager.generatePngImageData(secret);

        // Use swing utils to convert pngImageData to javaFX image
        InputStream iStream = new ByteArrayInputStream(pngImageData);
        Image image = new Image(iStream);

        // Create a new image view
        ImageView imageView = new ImageView(image);
        imageView.setId("qr-code");

        // Create a new label
        TextField secretLabel;
        if (AppConfig.TOTP_ALGORITHM.equals("SHA1")) {
            secretLabel = new TextField("Secret: " + secret);
        } else {
            secretLabel =
            new TextField("This App doesn't not support raw secret for " + AppConfig.TOTP_ALGORITHM + ".");
        }
        secretLabel.setEditable(false);
        secretLabel.getStyleClass().add("copyable-label");
        secretLabel.setAlignment(Pos.TOP_CENTER);

        formBox.getChildren().addAll(imageView, secretLabel);

        // We also need to confirm that they've done it correctly so create input box
        HBox confirmationComponents = new HBox();

        codeInput = new TextField();
        codeInput.setId("codeInput");

        LoginField enterCodeField = new LoginField(
            "Enter the code from your 2FA app:",
            codeInput,
            "e.g., 739123",
            "/images/lock.png"
        );

        // add the field to the confirmation components & center it
        confirmationComponents.getChildren().add(enterCodeField);
        confirmationComponents.setAlignment(Pos.TOP_CENTER);

        formBox.getChildren().add(confirmationComponents);

        BigButton confirm = new BigButton("Submit");
        confirm.setId("confirm");
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

    public void disable2FA(ActionEvent e) {
        User currentUser = sessionManager.getCurrentUser();
        AuthenticationDetails authDetails = currentUser.getAuthenticationDetails();
        authDetails.setTwoFactorEnabled(false);
        authDetails.setAuthenticationToken(null);
        authDetails.setRecoveryCodes(null);
        currentUser.setAuthenticationDetails(authDetails);

        if (databaseManager.update(authDetails) && databaseManager.update(currentUser)) {
            resultLabel.setText("2FA disabled successfully");
            resultLabel.setStyle("-fx-text-fill: green;");
            logger.info(this.sessionManager.getFullName() + " disabled 2FA");
            showRecoveryCodes = false;
            this.showSceneBetweenScenesThenNextScene("Successfully disabled 2FA", ProfileScreen.class, 1000);
        } else {
            resultLabel.setText("Failed to disable 2FA");
            resultLabel.setStyle("-fx-text-fill: red;");
            logger.warn(this.sessionManager.getFullName() + " failed to disable 2FA");
        }
    }

    @Override
    public void cleanup() {}
}
