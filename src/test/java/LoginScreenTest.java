import com.comp5590.App;
import com.comp5590.screens.LoginScreen;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.testfx.assertions.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.Set;

@ExtendWith(ApplicationExtension.class)  // TestFX Extension
public class LoginScreenTest extends SetupTests {
    App app;

    @Start  // This is similar to @BeforeAll it will run before all tests,
    // this is where we can get the stage and start the application
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
        app.getScreenManager().showScene(LoginScreen.class); // Force LoginScreen to show.
        stage.show();
    }

    private Pane getLoginScreen() {
        return (Pane) app.getScreenManager().getScreens().get(LoginScreen.class).getRoot();
    }

    /**
     * Test that the login screen has a login button
     */
    @Test
    public void testScreenHasLoginButton() {
        Pane loginScreen = getLoginScreen();
        Set<Node> loginButtons = loginScreen.lookupAll(".button");
        Assertions.assertThat(loginButtons).isNotNull();
        // Check at least one button has the text "Login"
        Assertions.assertThat(loginButtons).extracting("text").contains("Login");
    }

    /**
     * Test that the login screen has a password field
     */
    @Test
    public void testScreenHasPasswordField() {
        Pane loginScreen = getLoginScreen();
        Set<Node> passwordFields = loginScreen.lookupAll(".password-field");
        Assertions.assertThat(passwordFields).isNotNull();
        Assertions.assertThat(passwordFields).isNotEmpty();
    }
}
