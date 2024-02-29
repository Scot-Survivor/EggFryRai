import com.comp5590.configuration.AppConfig;
import com.comp5590.managers.secuirty.passwords.Argon2PasswordManager;
import com.comp5590.managers.secuirty.passwords.PasswordManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordTests extends SetupTests {
    @Test
    public void testArgonPasswordValid() {
        PasswordManager argonPasswordManager = new Argon2PasswordManager();
        argonPasswordManager.initialise();
        String password = "password";
        String hashedPassword = argonPasswordManager.hashPassword(password);
        assertTrue(argonPasswordManager.passwordMatches(hashedPassword, password));
    }

    @Test
    public void testArgonPasswordInvalid() {
        PasswordManager argonPasswordManager = new Argon2PasswordManager();
        argonPasswordManager.initialise();
        String password = "password";
        String hashedPassword = argonPasswordManager.hashPassword(password);
        assertFalse(argonPasswordManager.passwordMatches(hashedPassword, "password1"));
    }

}
