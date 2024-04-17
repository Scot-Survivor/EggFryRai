package com.comp5590.components.ProfileScreen;

import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.AuthenticationDetails;
import com.comp5590.database.entities.User;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UserInfoCard extends VBox {

    public UserInfoCard(User user) {
        // styling
        this.getStylesheets().add("/css/profile.css");

        // add styling
        this.getStyleClass().add("user-info-card");

        // grab all User sub-entities
        Address userAddress = user.getAddress();
        AuthenticationDetails userAuthDetails = user.getAuthenticationDetails();

        // make hbox for each user attribute
        HBox nameBox = new HBox();
        HBox emailBox = new HBox();
        HBox phoneBox = new HBox();
        HBox faxBox = new HBox();
        HBox addressBox = new HBox();
        HBox roleBox = new HBox();
        HBox communicationPreferenceBox = new HBox();
        HBox additionalNotesBox = new HBox();

        // add IDs for each
        nameBox.setId("name");
        emailBox.setId("email");
        phoneBox.setId("phone");
        faxBox.setId("fax");
        addressBox.setId("address");
        roleBox.setId("role");
        communicationPreferenceBox.setId("communicationPreference");
        additionalNotesBox.setId("additionalNotes");

        // add styles to each
        nameBox.getStyleClass().add("user-info-box");
        emailBox.getStyleClass().add("user-info-box");
        phoneBox.getStyleClass().add("user-info-box");
        faxBox.getStyleClass().add("user-info-box");
        addressBox.getStyleClass().add("user-info-box");
        roleBox.getStyleClass().add("user-info-box");
        communicationPreferenceBox.getStyleClass().add("user-info-box");
        additionalNotesBox.getStyleClass().add("user-info-box");

        // create default text to correspond to each user attribute
        Text nameText = new Text("Name: ");
        Text emailText = new Text("Email: ");
        Text phoneText = new Text("Phone: ");
        Text faxText = new Text("Fax: ");
        Text addressText = new Text("Address: ");
        Text roleText = new Text("Role: ");
        Text communicationPreferenceText = new Text("Communication Preference: ");
        Text additionalNotesText = new Text("Additional Notes: ");

        // put styles on every text
        nameText.getStyleClass().add("user-info-text");
        emailText.getStyleClass().add("user-info-text");
        phoneText.getStyleClass().add("user-info-text");
        faxText.getStyleClass().add("user-info-text");
        addressText.getStyleClass().add("user-info-text");
        roleText.getStyleClass().add("user-info-text");
        communicationPreferenceText.getStyleClass().add("user-info-text");
        additionalNotesText.getStyleClass().add("user-info-text");

        // create label w data for each user attribute OR set to N/A if no data
        Label nameData = new Label(user.getFirstName() + " " + user.getSurName());
        Label emailData = new Label(userAuthDetails.getEmail());
        Label phoneData = new Label(user.getPhone());
        Label faxData = new Label(user.getFax());
        Label addressData = new Label(
            userAddress.getAddressLineOne() +
            ", " +
            userAddress.getAddressLineTwo() +
            ", " +
            userAddress.getAddressLineThree() +
            ", " +
            userAddress.getPostCode()
        );
        Label roleData = new Label(user.getRole().toString());
        Label communicationPreferenceData = new Label(user.getCommunicationPreference().toString());
        Label additionalNotesData = new Label(user.getAdditionalNotes());

        if (user.getAdditionalNotes().equals("")) {
            additionalNotesData.setText("N/A");
        }
        if (user.getFax().equals("")) {
            faxData.setText("N/A");
        }

        // add text and data to each hbox
        nameBox.getChildren().addAll(nameText, nameData);
        emailBox.getChildren().addAll(emailText, emailData);
        phoneBox.getChildren().addAll(phoneText, phoneData);
        faxBox.getChildren().addAll(faxText, faxData);
        addressBox.getChildren().addAll(addressText, addressData);
        roleBox.getChildren().addAll(roleText, roleData);
        communicationPreferenceBox.getChildren().addAll(communicationPreferenceText, communicationPreferenceData);
        additionalNotesBox.getChildren().addAll(additionalNotesText, additionalNotesData);

        // add all hboxes to the card
        this.getChildren()
            .addAll(
                nameBox,
                emailBox,
                phoneBox,
                faxBox,
                addressBox,
                roleBox,
                communicationPreferenceBox,
                additionalNotesBox
            );
    }
}
