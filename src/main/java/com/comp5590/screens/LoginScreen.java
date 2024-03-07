package com.comp5590.screens;

import com.comp5590.entities.Patient;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import org.apache.logging.log4j.core.Logger;
import org.hibernate.Session;

import java.util.List;

import com.comp5590.components.LoginScreen.BigIcon;
import com.comp5590.components.LoginScreen.Paragraph;
// import title component
import com.comp5590.components.LoginScreen.Title;

/**
 * @author Rhys Walker
 */
public class LoginScreen extends AbstractScreen {
    private TextField email;
    private PasswordField password;
    private Label error;
    private final Logger logger = LoggerManager.getInstance().getLogger(LoginScreen.class);

    public LoginScreen(ScreenManager screenManager) { // exception needed for hashing
        super(screenManager);
    }

    @Override
    public void setup() {
        // load custom CSS
        this.cssPath = "/login.css";

        // create the grid pane object, apply styling & properties to it
        GridPane pane = new GridPane();
        pane.getStyleClass().add("custom-pane");
        pane.setPrefSize(250, 50);
        pane.setMaxSize(300, 400);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(20, 20, 20, 20)); // set the padding around the grid pane
        pane.setHgap(10); // set the horizontal gap between columns
        pane.setVgap(10); // set the vertical gap between rows

        // create child components, imported from the components folder
        HBox titleBox = new Title("Login");
        HBox paragraph = new Paragraph("Please login to track your appointments, prescriptions, and more.");
        VBox icon = new BigIcon("src/main/public/healthcare.png"); // goddamn disgusting image path took ages to fix
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
        GridPane.setValignment(loginBox, javafx.geometry.VPos.CENTER);

        // create the border pane (which will serve as root pane)
        // set grid pane as child of border pane
        BorderPane rootPane = new BorderPane();
        rootPane.setCenter(pane);

        setRootPane(rootPane); // set root pane
    }

    /**
     * A function that creates a button to send us to the POS screen
     *
     * @return The button
     */
    private VBox createLogin() {
        Label emailText = new Label("Email:");
        this.email = new TextField();
        this.email.setId("email");
        Label passwordText = new Label("Password:");
        this.password = new PasswordField();
        this.password.setId("password");
        this.error = new Label();
        this.error.setId("error");

        Button loginButton = new Button("Login");
        loginButton.setId("login");
        loginButton.setOnAction(this::login);

        VBox box = new VBox(emailText, email, passwordText, password, loginButton, this.error);
        box.setSpacing(10);

        return box;
    }

    private void login(ActionEvent event) {
        String email = this.email.getText();
        String password = this.password.getText();
        Session session = getSessionFactory().openSession();
        List<Patient> patients = session.createQuery("from Patient where email = :email", Patient.class)
                .setParameter("email", email)
                .list();
        session.close();
        if (patients.isEmpty()) {
            logger.error("Invalid Username({})", email);
            this.error.setText("Invalid Username or Password");
            return;
        }
        Patient patient = patients.get(0);
        boolean passwordValid = getApp().getPasswordManager().passwordMatches(patient.getPassword(), password);
        if (passwordValid) {
            if (!patient.isTwoFactorEnabled()) {
                getApp().getScreenManager().showScene(HomeScreen.class);
            } else {
                getApp().getScreenManager().showScene(MFAScreen.class);
            }
            // Set the current user here, but only if the password is valids
            getApp().setCurrentUser(patient);
        } else {
            logger.error("Invalid Password(*)");
            this.error.setText("Invalid Username or Password");
        }
    }

    public void setErrorText(String txt) {
        this.error.setText(txt);
    }
}
