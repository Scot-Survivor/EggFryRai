package com.comp5590.screens;

import com.comp5590.components.LoginScreen.BigButton;
import com.comp5590.components.LoginScreen.BigIcon;
import com.comp5590.components.LoginScreen.Paragraph;
import com.comp5590.components.LoginScreen.Title;
import com.comp5590.components.RegisterScreen.BackToLoginBox;
import com.comp5590.components.global.LineHorizontal;
import com.comp5590.components.global.LoginField;
import com.comp5590.components.global.SpaceVertical;
import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.AuthenticationDetails;
import com.comp5590.database.entities.User;
import com.comp5590.enums.CommunicationPreference;
import com.comp5590.enums.UserRole;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.core.Logger;

public class RegisterScreen extends AbstractScreen {

    // create the basic fields for storing info
    // * User
    private TextField email;
    private PasswordField password;
    private TextField firstName;
    private TextField surName;
    private TextField phone;
    private TextField fax;
    private TextField additionalNotes;

    // * Address
    private TextField addressLine1;
    private TextField addressLine2;
    private TextField addressLine3;
    private TextField country;
    private TextField postcode;

    // * Role (enum of PATIENT,DOCTOR)
    private ComboBox role;

    // * CommunicationPreference (enum of NONE,EMAIL,PHONE,FAX)
    private ComboBox communicationPreference;

    // * Other
    private Label error;
    private final Logger logger = LoggerManager.getInstance().getLogger(LoginScreen.class);

    public RegisterScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // load css
        this.addCss("/register.css");

        GridPane pane = new GridPane();
        pane.getStyleClass().add("custom-pane");

        // create child components, imported from the components folder
        HBox titleBox = new Title("Register");
        HBox paragraph = new Paragraph("Create an account today.");
        VBox icon = new BigIcon("/healthcare.png"); // create the image
        VBox registerBox = createRegisterBox();

        // add child components to our grid pane
        pane.add(titleBox, 0, 0);
        pane.add(paragraph, 0, 1);
        // set icon to column 1 but to span 2 rows, and always be to the far right of
        // the screen
        pane.add(icon, 1, 0, 1, 2);
        pane.add(registerBox, 0, 2);

        // allow register box to span across infinite rows and columns
        GridPane.setRowSpan(registerBox, Integer.MAX_VALUE);
        GridPane.setColumnSpan(registerBox, Integer.MAX_VALUE);

        // align the grid pane's contents to the center of the screen
        GridPane.setHalignment(titleBox, javafx.geometry.HPos.LEFT);
        GridPane.setHalignment(registerBox, javafx.geometry.HPos.CENTER);

        // add the column constraints, so icon will always be fixed to right of screen
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(Priority.ALWAYS); // Allow column 1 to grow to fill the available space
        pane.getColumnConstraints().add(column1);

        // create the border pane (which will serve as root pane)
        // set grid pane as child of border pane
        BorderPane rootPane = new BorderPane();
        rootPane.setPadding(new Insets(20));
        rootPane.setCenter(pane);

        setRootPane(rootPane); // set root pane
    }

    private VBox createRegisterBox() {
        // create the fields
        this.email = new TextField();
        this.email.setId("email");
        this.password = new PasswordField();
        this.password.setId("password");
        this.firstName = new TextField();
        this.firstName.setId("firstName");
        this.surName = new TextField();
        this.surName.setId("surName");
        this.phone = new TextField();
        this.phone.setId("phone");
        this.fax = new TextField();
        this.fax.setId("fax");
        this.additionalNotes = new TextField();
        this.additionalNotes.setId("additionalNotes");
        this.addressLine1 = new TextField();
        this.addressLine1.setId("addressLine1");
        this.addressLine2 = new TextField();
        this.addressLine2.setId("addressLine2");
        this.addressLine3 = new TextField();
        this.addressLine3.setId("addressLine3");
        this.country = new TextField();
        this.country.setId("country");
        this.postcode = new TextField();
        this.postcode.setId("postcode");
        this.role = new ComboBox();
        this.role.setId("role");
        this.communicationPreference = new ComboBox();
        this.communicationPreference.setId("communicationPreference");
        this.error = new Label();
        this.error.getStyleClass().add("error-label");

        // instantiate & create base fields for all the required patient info
        LoginField emailField = new LoginField("Email", this.email, "E.g. johndoe@gmail.com", "/at.png");
        LoginField passwordField = new LoginField("Password", this.password, "***************", "/lock.png");
        LoginField firstNameField = new LoginField("First Name", this.firstName, "E.g. John", "/user.png");
        LoginField surNameField = new LoginField("Surname", this.surName, "E.g. Doe", "/user.png");
        LoginField phoneField = new LoginField(
            "Phone (no country code or leading 0s)",
            this.phone,
            "E.g. 123456789",
            "/phone.png"
        );
        LoginField faxField = new LoginField("Fax (no dashes)", this.fax, "E.g. 123456789", "/fax.png");
        LoginField additionalNotesField = new LoginField(
            "Additional Notes",
            this.additionalNotes,
            "E.g. Allergies",
            "/notes.png"
        );
        LoginField addressLine1Field = new LoginField(
            "Address Line 1",
            this.addressLine1,
            "E.g. 123 Fake St",
            "/address.png"
        );
        LoginField addressLine2Field = new LoginField(
            "Address Line 2",
            this.addressLine2,
            "E.g. Canterbury",
            "/address.png"
        );
        LoginField addressLine3Field = new LoginField("Address Line 3", this.addressLine3, "E.g. Kent", "/address.png");
        LoginField countryField = new LoginField("Country", this.country, "E.g. UK", "/address.png");
        LoginField postcodeField = new LoginField("Postcode", this.postcode, "E.g. D01AB23", "/address.png");

        // create the role and communication preference fields
        this.role.getItems().addAll("Patient", "Doctor");
        this.communicationPreference.getItems().addAll("None", "Email", "Phone", "Fax");

        // create the register button
        BigButton registerButton = new BigButton();
        registerButton.setText("Register");

        // attach event listener to button, which runs register method
        registerButton.setOnAction(this::register);
        registerButton.setId("registerButton");

        // create horizontal line
        LineHorizontal line = new LineHorizontal(registerButton, 20, 3);

        // create back to login box button
        HBox backToRegisterScreenBox = new BackToLoginBox();
        backToRegisterScreenBox.setOnMouseClicked(event -> this.goToLoginPage());

        // create paddings
        SpaceVertical padding1 = new SpaceVertical(10);
        SpaceVertical padding2 = new SpaceVertical(20);
        SpaceVertical padding3 = new SpaceVertical(10);
        SpaceVertical padding4 = new SpaceVertical(7);
        SpaceVertical padding5 = new SpaceVertical(0);

        // make 3 children hboxes for storing all fields of required patient info
        VBox patientInfo1 = new VBox();
        VBox patientInfo2 = new VBox();
        VBox patientInfo3 = new VBox();

        patientInfo1.getStyleClass().add("patientInfoCols");
        patientInfo2.getStyleClass().add("patientInfoCols");
        patientInfo3.getStyleClass().add("patientInfoCols");

        // add the fields to the hboxes
        patientInfo1.getChildren().addAll(emailField, passwordField, role, communicationPreference);
        patientInfo2.getChildren().addAll(firstNameField, surNameField, phoneField, faxField, additionalNotesField);
        patientInfo3
            .getChildren()
            .addAll(addressLine1Field, addressLine2Field, addressLine3Field, countryField, postcodeField);

        // make a parent box for storing the above, and add the boxes to it horizontally
        HBox patientInfoFields = new HBox();
        patientInfoFields.getChildren().addAll(patientInfo1, patientInfo2, patientInfo3);
        patientInfoFields.getStyleClass().add("patientInfoFields");

        // add listeners to auto resize the above 3 boxes to 33% width of parent node on
        // window resize
        patientInfo1.prefWidthProperty().bind(patientInfoFields.widthProperty().divide(3));
        patientInfo2.prefWidthProperty().bind(patientInfoFields.widthProperty().divide(3));
        patientInfo3.prefWidthProperty().bind(patientInfoFields.widthProperty().divide(3));

        // add children nodes to the vbox this file will return
        VBox box = new VBox();
        box
            .getChildren()
            .addAll(
                padding1,
                patientInfoFields,
                padding2,
                registerButton,
                padding3,
                padding4,
                backToRegisterScreenBox,
                padding5,
                error
            );

        // set line to be added LATER, inserted vertically beneath the padding3 box
        Platform.runLater(() -> {
            box.getChildren().add(box.getChildren().indexOf(padding3) + 1, line);
        });

        // set box properties cos why not
        box.setAlignment(Pos.CENTER);
        box.setSpacing(10);

        return box;
    }

    private void goToLoginPage() {
        showScene(LoginScreen.class);
        clearFields();
        unsetErrorText();
    }

    private void register(ActionEvent event) {
        // check if fields are null
        if (
            this.email == null ||
            this.password == null ||
            this.firstName == null ||
            this.surName == null ||
            this.phone == null ||
            this.addressLine1 == null ||
            this.country == null ||
            this.postcode == null ||
            this.role.getValue() == null ||
            this.communicationPreference.getValue() == null
        ) {
            this.logger.error("One or more fields are null.");
            this.error.setText("Please ensure all fields are filled in.");
            return;
        }

        // grab all fields
        String email = this.email.getText().trim().replaceAll("\\s", "");
        String password = this.password.getText().trim();
        String firstName = this.firstName.getText().trim();
        String surName = this.surName.getText().trim();
        String phone = this.phone.getText().replaceAll("\\s", "");
        String fax = this.fax.getText().replaceAll("\\s", "");
        String additionalNotes = this.additionalNotes.getText().trim();
        String addressLine1 = this.addressLine1.getText().trim();
        String addressLine2 = this.addressLine2.getText().trim();
        String addressLine3 = this.addressLine3.getText().trim();
        String country = this.country.getText().trim();
        String postcode = this.postcode.getText().replaceAll("\\s", "");
        String role = this.role.getValue().toString().toUpperCase();
        String communicationPreference = this.communicationPreference.getValue().toString().toUpperCase();
        // check if fields are empty
        if (
            email.isEmpty() ||
            password.isEmpty() ||
            firstName.isEmpty() ||
            surName.isEmpty() ||
            phone.isEmpty() ||
            addressLine1.isEmpty() ||
            country.isEmpty() ||
            postcode.isEmpty() ||
            role.isEmpty() ||
            communicationPreference.isEmpty()
        ) {
            this.logger.warn("Please ensure all fields are filled in.");
            this.error.setText("Please ensure all fields are filled in.");
            return;
        }

        // check if user already exists
        User user = getDatabaseManager().getByProperty(User.class, "authenticationDetails.email", email);

        if (user != null) {
            logger.error("User already exists: {}", email);
            this.error.setText("User already exists.");
            return;
        }

        // * validate all the fields
        // validation: format of fields
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            logger.error("Invalid email address: {}", email);
            this.error.setText("Invalid email address.");
            return;
        }

        if (!phone.matches("^[0-9]{10}$")) {
            logger.error("Invalid phone number: {}", phone);
            this.error.setText("Invalid phone number.");
            return;
        }

        if (!fax.matches("^[0-9]{10}$")) {
            logger.error("Invalid fax number: {}", fax);
            this.error.setText("Invalid fax number.");
            return;
        }

        if (!postcode.matches("^[A-Z0-9]{6}$")) {
            logger.error("Invalid postcode: {}", postcode);
            this.error.setText("Invalid postcode.");
            return;
        }

        // validation: lengths of fields
        if (email.length() > 50) {
            logger.error("Email is too long: {}", email);
            this.error.setText("Email is too long.");
            return;
        }

        if (password.length() > 1024) {
            logger.error("Password is too long: {}", password);
            return;
        }

        if (fax.length() > 20) {
            logger.error("Fax number is too long: {}", fax);
            this.error.setText("Fax number is too long.");
            return;
        }

        if (phone.length() > 20) {
            logger.error("Phone number is too long: {}", phone);
            this.error.setText("Phone number is too long.");
            return;
        }

        if (firstName.length() > 50) {
            logger.error("First name is too long: {}", firstName);
            this.error.setText("First name is too long.");
            return;
        }

        if (surName.length() > 50) {
            logger.error("Surname is too long: {}", surName);
            this.error.setText("Surname is too long.");
            return;
        }

        if (additionalNotes.length() > 1024) {
            logger.error("Additional notes are too long: {}", additionalNotes);
            this.error.setText("Additional notes are too long.");
            return;
        }

        if (addressLine1.length() > 1024) {
            logger.error("Address Line 1 is too long: {}", addressLine1);
            this.error.setText("Address Line 1 is too long.");
            return;
        }

        if (addressLine2.length() > 1024) {
            logger.error("Address Line 2 is too long: {}", addressLine2);
            this.error.setText("Address Line 2 is too long.");
            return;
        }

        if (addressLine3.length() > 1024) {
            logger.error("Address Line 3 is too long: {}", addressLine3);
            this.error.setText("Address Line 3 is too long.");
            return;
        }

        if (country.length() > 255) {
            logger.error("Country name is too long: {}", country);
            this.error.setText("Country name is too long.");
            return;
        }

        // * if all checks above pass, create a new user, with the proper models
        // hash the password
        password = getApp().getPasswordManager().hashPassword(password);

        logger.error("Password hash is: {}", password);

        logger.error("Chars long: {}", password.length());
        // create new auth details
        AuthenticationDetails authDetails = new AuthenticationDetails(email, password, false, null, null);

        // (by default 2FA is disabled, but can be enabled in settings)

        // create new address
        Address address = new Address(addressLine1, addressLine2, addressLine3, country, postcode);
        // create new user
        user =
        new User(
            firstName,
            surName,
            phone,
            fax,
            additionalNotes,
            CommunicationPreference.valueOf(communicationPreference),
            UserRole.valueOf(role),
            address
        );

        int savedAddressId = getDatabaseManager().saveGetId(address);

        Address savedAddress = getDatabaseManager().get(Address.class, savedAddressId);
        if (savedAddress == null) {
            logger.error("Failed to save address to database.");
            this.error.setText("Failed to save address to database.");
            return;
        }
        // save auth entity
        int savedAuthDetailsId = getDatabaseManager().saveGetId(authDetails);

        AuthenticationDetails savedAuthDetails = getDatabaseManager()
            .get(AuthenticationDetails.class, savedAuthDetailsId);
        if (savedAuthDetails == null) {
            logger.error("Failed to save auth details to database.");
            this.error.setText("Failed to save auth details to database.");
            return;
        }
        // set the auth details and address to the user entity (one <-> one
        // relationship)
        user.setAuthenticationDetails(authDetails);

        user.setAddress(address);
        // save the user to the database
        int savedUserId = getDatabaseManager().saveGetId(user);

        user = getDatabaseManager().get(User.class, savedUserId);

        if (user == null) {
            logger.error("Failed to save user to database.");
            this.error.setText("Failed to save user to database.");
            return;
        }

        // log creation of new user
        logger.info("New user registered: {}", email);

        // redirect user to InBetweenScreensScreen after successful registration, with
        // success message
        this.showSceneBetweenScenesThenNextScene(
                "✅ You have successfully registered.\nRedirecting to login page...",
                3,
                LoginScreen.class
            );

        // [cleanup] clear all fields & unset error text
        clearFields();

        unsetErrorText();
    }

    public void setErrorText(String txt) {
        this.error.setText(txt);
    }

    public void unsetErrorText() {
        this.error.setText("");
    }

    public void clearFields() {
        this.email.clear();
        this.password.clear();
        this.firstName.clear();
        this.surName.clear();
        this.phone.clear();
        this.fax.clear();
        this.additionalNotes.clear();
        this.addressLine1.clear();
        this.addressLine2.clear();
        this.addressLine3.clear();
        this.country.clear();
        this.postcode.clear();
        this.role.getSelectionModel().clearSelection();
        this.communicationPreference.getSelectionModel().clearSelection();
    }
}
