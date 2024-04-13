package com.comp5590.screens.general.settings;

import com.comp5590.database.entities.AuthenticationDetails;
import com.comp5590.database.entities.User;
import com.comp5590.events.enums.UserAttribute;
import com.comp5590.events.eventtypes.users.UserUpdateEvent;
import com.comp5590.events.managers.EventManager;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.managers.ScreenManager;
import com.comp5590.screens.misc.AbstractScreen;
import com.comp5590.security.authentication.annotations.AuthRequired;
import com.comp5590.security.managers.passwords.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

@AuthRequired
public class ProfileScreen extends AbstractScreen {

    private final SessionManager sessionManager = SessionManager.getInstance();
    private final PasswordManager passwordManager = PasswordManager.getInstance();
    private final EventManager eventManager = EventManager.getInstance();
    private Label messageLabel;

    public ProfileScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        GridPane pane = attachDefaultPane();
        attachHeaderAndNavBar("Your Profile");

        // display user information
        User currentUser = sessionManager.getCurrentUser();
        AuthenticationDetails authDetails = currentUser.getAuthenticationDetails();

        Text fullNameText = new Text("Full Name: " + sessionManager.getFullName());
        Text phoneText = new Text("Phone: " + currentUser.getPhone());
        Text faxText = new Text("Fax: " + currentUser.getFax());
        Text addressLine1Text = new Text("Address Line 1: " + currentUser.getAddress().getAddressLineOne());
        Text addressLine2Text = new Text("Town/City: " + currentUser.getAddress().getAddressLineTwo());
        Text addressLine3Text = new Text("County: " + currentUser.getAddress().getAddressLineThree());
        Text addressCountryText = new Text("Country: " + currentUser.getAddress().getCountry());
        Text addressPCText = new Text("Postcode: " + currentUser.getAddress().getPostCode());
        Text emailText = new Text("Email: " + authDetails.getEmail());
        Text isMFAEnabledText = new Text("MFA Enabled: " + (authDetails.isTwoFactorEnabled() ? "Yes" : "No"));
        Button goToMFASettings = new Button("Go to MFA Settings");
        messageLabel = new Label();

        TextField newEmailField = new TextField();
        newEmailField.setPromptText("New Email");
        newEmailField.setId("newEmailField");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New Password");
        newPasswordField.setId("newPasswordField");

        Button applyEmailButton = new Button("Apply Email");
        applyEmailButton.setId("applyEmailButton");
        applyEmailButton.setOnAction(event -> {
            String newEmail = newEmailField.getText().trim();
            if (!newEmail.isEmpty()) {
                authDetails.setEmail(newEmail);
                // save the updated authentication details
                if (getDatabaseManager().update(authDetails)) {
                    // notify user of successful update and add to logs
                    getLogger().info("Email change successful");
                    messageLabel.setText("Email change successful.");
                    eventManager.callEvent(new UserUpdateEvent(UserAttribute.EMAIL, newEmail, currentUser.getId()));
                } else {
                    getLogger().error("Email change failed");
                    messageLabel.setText("Email change failed.");
                }
            } else {
                messageLabel.setText("Email cannot be empty.");
            }
        });

        Button applyPasswordButton = new Button("Apply Password");
        applyPasswordButton.setId("applyPasswordButton");
        applyPasswordButton.setOnAction(event -> {
            String newPassword = newPasswordField.getText();
            if (!newPassword.isEmpty()) {
                authDetails.setPassword(passwordManager.hashPassword(newPassword));
                currentUser.setAuthenticationDetails(authDetails);
                // save the updated authentication details
                if (getDatabaseManager().update(authDetails) && getDatabaseManager().update(currentUser)) {
                    getLogger().info("Password change successful.");
                    messageLabel.setText("Password change successful.");
                    eventManager.callEvent(
                        new UserUpdateEvent(UserAttribute.PASSWORD, "REDACTED", currentUser.getId())
                    );
                } else {
                    getLogger().error("Password change failed.");
                    messageLabel.setText("Password change failed.");
                }
            } else {
                messageLabel.setText("Password cannot be empty.");
            }
        });

        goToMFASettings.setOnAction(event -> showScene(MFASettingsScreen.class));

        pane.add(fullNameText, 0, 3);
        pane.add(phoneText, 0, 4);
        pane.add(faxText, 0, 5);
        pane.add(addressLine1Text, 0, 6);
        pane.add(addressLine2Text, 0, 7);
        pane.add(addressLine3Text, 0, 8);
        pane.add(addressCountryText, 0, 9);
        pane.add(addressPCText, 0, 10);
        pane.add(emailText, 0, 11);
        pane.add(isMFAEnabledText, 0, 12);
        pane.add(newEmailField, 0, 13);
        pane.add(applyEmailButton, 1, 14);
        pane.add(newPasswordField, 0, 15);
        pane.add(applyPasswordButton, 1, 16);
        pane.add(messageLabel, 0, 17);
        pane.add(goToMFASettings, 0, 18);
    }

    @Override
    public void cleanup() {
        messageLabel.setText("");
    }
}
