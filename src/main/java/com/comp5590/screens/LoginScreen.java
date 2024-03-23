package com.comp5590.screens;

import com.comp5590.components.LoginScreen.BigButton;
import com.comp5590.components.LoginScreen.BigIcon;
import com.comp5590.components.LoginScreen.ForgotPasswordButton;
import com.comp5590.components.LoginScreen.Paragraph;
import com.comp5590.components.LoginScreen.RegisterBox;
// import title component
import com.comp5590.components.LoginScreen.Title;
import com.comp5590.components.global.LineHorizontal;
import com.comp5590.components.global.LoginField;
import com.comp5590.components.global.SpaceVertical;
import com.comp5590.database.entities.User;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
        this.addCss("/login.css");

        // create the grid pane object, apply styling & properties to it
        GridPane pane = new GridPane();
        pane.getStyleClass().add("custom-pane");

        // create child components, imported from the components folder
        HBox titleBox = new Title("Login");
        HBox paragraph = new Paragraph("Please login to track your appointments, prescriptions, and more.");
        VBox icon = new BigIcon("/healthcare.png"); // create the image
        VBox loginBox = createLoginBox();

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

        // add the column constraints, so icon will always be fixed to right of screen
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(Priority.ALWAYS); // Allow column 1 to grow to fill the available space
        pane.getColumnConstraints().add(column1);

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
    private VBox createLoginBox() {
        // create email & password fields, binding them to this class's object instance
        this.email = new TextField();
        this.password = new PasswordField();
        this.error = new Label();
        this.error.getStyleClass().add("error-label");
        email.setId("email");
        password.setId("password");
        error.setId("error");

        // create login & password field objects, passing in some basic info
        LoginField emailField = new LoginField("Email", this.email, "E.g. johndoe@gmail.com", "/at.png");
        LoginField passwordField = new LoginField("Password", this.password, "***************", "/lock.png");

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
        Line line = new LineHorizontal(loginButton, 20, 3);

        // create button for registering new account
        HBox backToRegisterScreenBox = new RegisterBox();
        backToRegisterScreenBox.setId("backToRegisterScreenBox");
        // add event listener to register box
        backToRegisterScreenBox.setOnMouseClicked(event -> this.gotoRegisterPage());

        // create vboxes for margin
        SpaceVertical padding1 = new SpaceVertical(20);
        SpaceVertical padding2 = new SpaceVertical(10);
        SpaceVertical padding3 = new SpaceVertical(10);
        SpaceVertical padding4 = new SpaceVertical(7);
        SpaceVertical padding5 = new SpaceVertical(0);

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

    private void login(ActionEvent event) {
        String email = this.email.getText().trim();
        String password = this.password.getText().trim();
        User user = getDatabaseManager().getByProperty(User.class, "authenticationDetails.email", email);
        if (user == null) {
            logger.error("Invalid Email({})", email);
            this.error.setText("Invalid Email or Password");
            return;
        }
        boolean passwordValid = getApp()
            .getPasswordManager()
            .passwordMatches(user.getAuthenticationDetails().getPassword(), password);
        if (passwordValid) {
            if (!user.getAuthenticationDetails().isTwoFactorEnabled()) {
                // set the user as authenticated in session manager
                getApp().getSessionManager().setAuthenticated(true);
                // show the user the home screen (successfully logged in)
                showScene(HomeScreen.class);
                logger.info("User is logged in successfully. as {}", user.getAuthenticationDetails().getEmail());
            } else {
                showScene(MFAScreen.class);
            }
            // Set the current user here, but only if the password is valids
            getApp().setCurrentUser(user);
        } else {
            logger.error("Invalid Password(*)");
            this.error.setText("Invalid password. Please try again.");
        }
    }

    private void gotoRegisterPage() {
        showScene(RegisterScreen.class); // show the register pagee
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
    }

    @Override
    public void cleanup() {
        this.email.clear();
        this.password.clear();
        this.error.setText("");
    }
}
