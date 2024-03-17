package com.comp5590.screens;

import com.comp5590.components.LoginScreen.BigButton;
import com.comp5590.components.LoginScreen.BigIcon;
import com.comp5590.components.LoginScreen.ForgotPasswordButton;
import com.comp5590.components.LoginScreen.LoginField;
import com.comp5590.components.LoginScreen.Paragraph;
// import title component
import com.comp5590.components.LoginScreen.Title;
import com.comp5590.database.entities.User;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import org.apache.logging.log4j.core.Logger;

/**
 * @author Rhys Walker
 */
public class LoginScreen extends AbstractScreen {

    private TextField email;
    private PasswordField password;
    private Label error;
    private final Logger logger = LoggerManager.getInstance().getLogger(LoginScreen.class);

    public LoginScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // load custom CSS
        this.cssPath = "/login.css";

        // create the grid pane object, apply styling & properties to it
        GridPane pane = new GridPane();
        pane.getStyleClass().add("custom-pane");
        pane.setPrefSize(250, 400);
        // set max width to 250, and max height to fit content
        pane.setMaxSize(250, 600);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(20, 20, 20, 20)); // set the padding around the grid pane
        pane.setHgap(10); // set the horizontal gap between columns
        pane.setVgap(10); // set the vertical gap between rows

        // create child components, imported from the components folder
        HBox titleBox = new Title("Login");
        HBox paragraph = new Paragraph("Please login to track your appointments, prescriptions, and more.");
        VBox icon = new BigIcon("/healthcare.png"); // create the image
        VBox loginBox = createLogin();

        // add child components to our grid pane
        pane.add(titleBox, 0, 0);
        pane.add(paragraph, 0, 1);
        pane.add(icon, 1, 0);
        GridPane.setRowSpan(icon, 2);
        pane.add(loginBox, 0, 2);
        GridPane.setColumnSpan(loginBox, 2);

        // align the grid pane's contents to the center of the screen
        GridPane.setHalignment(titleBox, javafx.geometry.HPos.LEFT);
        GridPane.setHalignment(loginBox, javafx.geometry.HPos.CENTER);

        // create the border pane (which will serve as root pane)
        // set grid pane as child of border pane
        BorderPane rootPane = new BorderPane();
        rootPane.setCenter(pane);

        setRootPane(rootPane); // set root pane
    }

    /**
     * A function that creates a button to send us to the POS screen
     * NOTE: must keep this in the LoginScreen file due to certain event listeners
     * being bound to the textfields, which are themselves fields of this class.
     *
     * @return The button
     */
    private VBox createLogin() {
        // create email & password fields, binding them to this class's object instance
        this.email = new TextField();
        this.email.setId("email");
        this.password = new PasswordField();
        this.password.setId("password");
        this.error = new Label();
        this.error.setId("error");

        // create login & password field objects, passing in some basic info
        LoginField emailField = new LoginField("Email", email, "E.g. johndoe@gmail.com", "/at.png");
        LoginField passwordField = new LoginField("Password", password, "***************", "/lock.png");

        // forgot password box
        ForgotPasswordButton forgotPasswordButton = new ForgotPasswordButton("Forgot Password?");
        // add the forgot password button to a vbox
        VBox forgotPasswordBtn = new VBox();
        forgotPasswordBtn.getChildren().add(forgotPasswordButton);

        // create a login button
        BigButton loginButton = new BigButton();
        loginButton.setId("login");
        loginButton.setOnAction(this::login);
        // add the login button to a vbox
        VBox finalLoginBtn = new VBox();
        finalLoginBtn.getChildren().add(loginButton);

        // create horizontal line, the same length as loginButton
        Line line = new Line(10, 0, loginButton.getWidth(), 0);
        line.getStyleClass().add("line");
        loginButton
            .widthProperty()
            .addListener((obs, oldVal, newVal) -> {
                line.setEndX(newVal.doubleValue() - 10);
            });

        // create vboxes for margin
        VBox padding1 = new VBox();
        padding1.setPrefHeight(20);
        VBox padding2 = new VBox();
        padding2.setPrefHeight(10);
        VBox padding3 = new VBox();
        padding3.setPrefHeight(10);

        // add children nodes to the vbox this file will return
        VBox box = new VBox();
        box
            .getChildren()
            .addAll(
                padding1,
                emailField,
                passwordField,
                forgotPasswordBtn,
                padding2,
                finalLoginBtn,
                padding3,
                line,
                error
            );

        // set box properties cos why not
        box.setAlignment(Pos.CENTER);
        box.setSpacing(10);

        return box;
    }

    private void login(ActionEvent event) {
        String email = this.email.getText().trim();
        String password = this.password.getText().trim();
        User user = getDatabaseManager().getByProperty(User.class, "authenticationDetails.email", email);
        if (user == null) {
            logger.error("Invalid Username({})", email);
            this.error.setText("Invalid Username or Password");
            return;
        }
        boolean passwordValid = getApp()
            .getPasswordManager()
            .passwordMatches(user.getAuthenticationDetails().getPassword(), password);
        if (passwordValid) {
            if (!user.getAuthenticationDetails().isTwoFactorEnabled()) {
                // set the user as authenticated in session manager
                getApp().getSessionManager().setAuthenticated(true);
                // unauthenticate user after 2 hours, forcing them to re-login
                getApp().getSessionManager().unauthenticateAfter(2);
                // show the user the home screen (successfully logged in)
                getApp().getScreenManager().showScene(HomeScreen.class);
                logger.info("User is logged in successfully. as {}", user.getAuthenticationDetails().getEmail());
            } else {
                getApp().getScreenManager().showScene(MFAScreen.class);
            }
            // Set the current user here, but only if the password is valids
            getApp().setCurrentUser(user);
        } else {
            logger.error("Invalid Password(*)");
            this.error.setText("Invalid password. Please try again.");
        }
    }

    public void setErrorText(String txt) {
        this.error.setText(txt);
    }
}
