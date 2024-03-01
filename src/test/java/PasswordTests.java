import com.comp5590.configuration.AppConfig;
import com.comp5590.managers.secuirty.passwords.Argon2PasswordManager;
import com.comp5590.managers.secuirty.passwords.PasswordManager;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Order(3)
public class PasswordTests extends SetupTests {

    @Test
    public void testPasswordInstanceOf() {
        PasswordManager pm = PasswordManager.getInstanceOf(AppConfig.getInstance().HASH_ALGORITHM);
        assertInstanceOf(Argon2PasswordManager.class, pm);
    }

    @Test
    public void testPasswordInstanceOfNull() {
        PasswordManager pm = PasswordManager.getInstanceOf("THIS_IS_NOT_A_VALID_PASSWORD_MANAGER");
        assertNull(pm);
    }

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
