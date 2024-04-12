package com.comp5590.screens;

import com.comp5590.configuration.AppConfig;
import com.comp5590.database.entities.AuthenticationDetails;
import com.comp5590.database.entities.User;
import com.comp5590.managers.ScreenManager;
import com.comp5590.managers.SessionManager;
import com.comp5590.security.managers.authentication.annotations.AuthRequired;
import com.comp5590.security.managers.passwords.*;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

@AuthRequired
public class ProfileScreen extends AbstractScreen {

    private final SessionManager sessionManager = SessionManager.getInstance();
    private final PasswordManager passwordManager = PasswordManager.getInstanceOf(AppConfig.HASH_ALGORITHM);

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
                getDatabaseManager().update(authDetails);
                // notify user of successful update and add to logs
                System.out.println("Email change successful.");
                getLogger().info("Email change successful: New email is {}", newEmail);
            }
        });

        Button applyPasswordButton = new Button("Apply Password");
        applyPasswordButton.setId("applyPasswordButton");
        applyPasswordButton.setOnAction(event -> {
            String newPassword = newPasswordField.getText();
            if (!newPassword.isEmpty()) {
                authDetails.setPassword(PasswordManager.getInstance().hashPassword(newPassword));
                currentUser.setAuthenticationDetails(authDetails);
                // save the updated authentication details
                if (getDatabaseManager().update(authDetails) && getDatabaseManager().update(currentUser)) {
                    getLogger().info("Password change successful.");
                } else {
                    getLogger().error("Password change failed.");
                }
            }
        });

        pane.add(fullNameText, 0, 3);
        pane.add(phoneText, 0, 4);
        pane.add(faxText, 0, 5);
        pane.add(addressLine1Text, 0, 6);
        pane.add(addressLine2Text, 0, 7);
        pane.add(addressLine3Text, 0, 8);
        pane.add(addressCountryText, 0, 9);
        pane.add(addressPCText, 0, 10);
        pane.add(emailText, 0, 11);
        pane.add(newEmailField, 0, 12);
        pane.add(applyEmailButton, 1, 12);
        pane.add(newPasswordField, 0, 13);
        pane.add(applyPasswordButton, 1, 13);
    }

    @Override
    public void cleanup() {}
}
