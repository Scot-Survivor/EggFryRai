package com.comp5590.screens.authentication;

import com.comp5590.components.LoginScreen.BigButton;
import com.comp5590.components.LoginScreen.BigIcon;
import com.comp5590.components.LoginScreen.Paragraph;
import com.comp5590.components.LoginScreen.Title;
import com.comp5590.components.global.LoginField;
import com.comp5590.components.global.SpaceVertical;
import com.comp5590.database.entities.User;
import com.comp5590.managers.LoggerManager;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.general.HomeScreen;
import com.comp5590.screens.managers.ScreenManager;
import com.comp5590.screens.misc.AbstractScreen;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.apache.logging.log4j.core.Logger;

public class MFAScreen extends AbstractScreen {

    private TextField code;
    private Label error;

    private final Logger logger = LoggerManager.getInstance().getLogger(LoginScreen.class);

    public MFAScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void setup() {
        this.addCss("/css/login.css");

        GridPane pane = new GridPane();
        pane.getStyleClass().add("custom-pane");

        HBox titleBox = new Title("Login");
        HBox paragraph = new Paragraph("Please login to track your appointments, prescriptions, and more.");
        VBox icon = new BigIcon("/images/healthcare.png"); // create the image
        VBox codeBox = create2FA();

        pane.add(titleBox, 0, 0);
        pane.add(paragraph, 0, 1);
        pane.add(icon, 1, 0);
        GridPane.setRowSpan(icon, 2);
        pane.add(codeBox, 0, 2);
        GridPane.setColumnSpan(codeBox, 2);

        // align the grid pane's contents to the center of the screen
        GridPane.setHalignment(titleBox, javafx.geometry.HPos.LEFT);
        GridPane.setHalignment(codeBox, javafx.geometry.HPos.CENTER);

        // add the column constraints, so icon will always be fixed to right of screen
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(Priority.ALWAYS); // Allow column 1 to grow to fill the available space
        pane.getColumnConstraints().add(column1);

        // create the border pane (which will   serve as root pane)
        // set grid pane as child of border pane
        BorderPane rootPane = new BorderPane();
        rootPane.setCenter(pane);

        setRootPane(rootPane); // set root pane
    }

    /**
     * Create the 2FA code input form
     * @return HBox
     */
    private VBox create2FA() {
        this.code = new TextField();
        this.code.setId("code");

        LoginField codeField = new LoginField("2FA Code", this.code, "", "/images/lock.png");

        this.error = new Label();
        this.error.setId("error");
        this.error.getStyleClass().add("error-label");

        BigButton submit = new BigButton();
        submit.setId("submit");
        submit.setOnAction(this::submitCode);

        VBox finalCodeButton = new VBox();
        finalCodeButton.getChildren().add(submit);

        // create vboxes for margin
        SpaceVertical padding1 = new SpaceVertical(20);
        SpaceVertical padding2 = new SpaceVertical(10);
        SpaceVertical padding3 = new SpaceVertical(10);

        VBox box = new VBox();
        box.getChildren().addAll(padding1, codeField, padding2, finalCodeButton, padding3, error);

        // set box properties cos why not
        box.setAlignment(Pos.CENTER);
        box.setSpacing(10);
        return box;
    }

    /**
     * Submit the 2FA code
     * @param e ActionEvent
     */
    private void submitCode(ActionEvent e) {
        String code = this.code.getText();
        User user = SessionManager.getInstance().getCurrentUser();
        boolean valid = this.verify(code, user);
        if (valid) {
            // set the user as authenticated in session manager
            getApp().getSessionManager().authenticate(user);
            // show the home screen
            showScene(HomeScreen.class);
            logger.info("User is logged in successfully. as {}", user.getAuthenticationDetails().getEmail());
        } else {
            LoginScreen loginScreen = (LoginScreen) getApp().getScreenManager().getScreenInstance(LoginScreen.class);
            loginScreen.setErrorText("Invalid 2FA code");
            showScene(LoginScreen.class);
            SessionManager.getInstance().setCurrentUser(null); // clear the current user
        }
    }

    /**
     * Verify the 2FA code
     * @param code 2FA code
     * @param user User object
     * @return boolean
     */
    private boolean verify(String code, User user) {
        return (
            getApp().getTotpManager().verifyRecoveryCode(user.getAuthenticationDetails().getRecoveryCodes(), code) ||
            getApp().getTotpManager().verifyCode(user.getAuthenticationDetails().getAuthenticationToken(), code)
        );
    }

    private HBox createTitle() {
        Text text = new Text("Enter your 2FA below");
        return new HBox(text);
    }

    @Override
    public void cleanup() {
        this.code.clear();
        this.error.setText("");
    }
}
