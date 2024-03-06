package com.comp5590.screens;

import com.comp5590.App;
import com.comp5590.entities.Patient;
import com.comp5590.managers.DatabaseManager;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import org.apache.logging.log4j.core.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * @author Rhys Walker
 */
public class LoginScreen extends AbstractScreen {
    private final SessionFactory sessionFactory;
    private final App app;
    private TextField email;
    private PasswordField password;
    private Label error;
    private final Logger logger = LoggerManager.getInstance().getLogger(LoginScreen.class);

    public LoginScreen(ScreenManager screenManager){ // exception needed for hashing
        super(screenManager);
        this.sessionFactory = DatabaseManager.getInstance().getSessionFactory();
        this.app = App.getInstance();
    }

    @Override
    public void setup() {
        // create the border pane object and add the login to the center
        BorderPane pane = new BorderPane();
        pane.setCenter(createLogin());
        pane.setTop(createTitle());
        setRootPane(pane);
        this.cssPath = "/login.css";
    }

    /**
     * A function that creates a button to send us to the POS screen
     * @return The button
     */
    private VBox createLogin(){
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

        return new VBox(emailText, email, passwordText, password, loginButton, this.error);
    }

    private void login(ActionEvent event){
        String email = this.email.getText();
        String password = this.password.getText();
        Session session = sessionFactory.openSession();
        List<Patient> patients = session.createQuery("from Patient where email = :email", Patient.class)
                .setParameter("email", email)
                .list();
        session.close();
        if (patients.isEmpty()){
            logger.error("Invalid Username({})", email);
            this.error.setText("Invalid Username or Password");
            return;
        }
        Patient patient = patients.get(0);
        boolean passwordValid  = app.getPasswordManager().passwordMatches(patient.getPassword(), password);
        if (passwordValid){
            if (!patient.isTwoFactorEnabled()) {
                app.getScreenManager().showScene(HomeScreen.class);
            } else {
                app.getScreenManager().showScene(MFAScreen.class);
            }
            // Set the current user here, but only if the password is valids
            app.setCurrentUser(patient);
        } else {
            logger.error("Invalid Password(*)");
            this.error.setText("Invalid Username or Password");
        }
    }

    // Create title
    private HBox createTitle(){
        Text text = new Text("Please Login");
        text.getStyleClass().add("title-text");
        HBox titleBox = new HBox(text);
        titleBox.getStyleClass().add("title");
        return titleBox;
    }

    public void setErrorText(String txt) {
        this.error.setText(txt);
    }
}
