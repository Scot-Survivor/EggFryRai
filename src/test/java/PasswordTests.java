import com.comp5590.managers.secuirty.ArgonPasswordManager;
import com.comp5590.managers.secuirty.PasswordManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordTests extends SetupTests {
    PasswordManager argonPasswordManager = new ArgonPasswordManager();

    @Test
    public void testPasswordHashing() {
        argonPasswordManager.initialise();
        String password = "password";
        String hashedPassword = argonPasswordManager.hashPassword(password);
        assertTrue(argonPasswordManager.passwordMatches(hashedPassword, password));
    }

}
