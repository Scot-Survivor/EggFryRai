package com.comp5590.screens;

import com.comp5590.components.LoginScreen.BigButton;
import com.comp5590.components.LoginScreen.BigIcon;
import com.comp5590.components.LoginScreen.Paragraph;
import com.comp5590.components.LoginScreen.Title;
import com.comp5590.components.RegisterScreen.BackToLoginBox;
import com.comp5590.components.global.LineHorizontal;
import com.comp5590.components.global.LoginField;
import com.comp5590.components.global.SpaceVertical;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.ScreenManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
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
    private LoginField email;
    private LoginField username;
    private LoginField password;
    private Label error;
    private final Logger logger = LoggerManager.getInstance().getLogger(LoginScreen.class);

    public RegisterScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        // load css
        this.cssPath = "/register.css";

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
        GridPane.setColumnSpan(registerBox, 2);

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
        rootPane.setCenter(pane);

        setRootPane(rootPane); // set root pane
    }

    private VBox createRegisterBox() {
        // create the fields
        this.email = new LoginField("Email", new TextField(), "Email", "/at.png");
        this.email.setId("email");
        this.username = new LoginField("Username", new TextField(), "Username", "/user.png");
        this.username.setId("username");
        this.password = new LoginField("Password", new PasswordField(), "***************", "/lock.png");
        this.password.setId("password");
        this.error = new Label();
        this.error.setId("error");

        // create the register button
        BigButton registerButton = new BigButton();
        registerButton.setText("Register");

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
        SpaceVertical padding5 = new SpaceVertical(3);

        // add children nodes to the vbox this file will return
        VBox box = new VBox();
        box
            .getChildren()
            .addAll(
                padding1,
                email,
                username,
                password,
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
        getApp().getScreenManager().showScene(LoginScreen.class);
    }

    private void register(ActionEvent event) {}
}
