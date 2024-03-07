package com.comp5590.tests.basic;

import static org.junit.jupiter.api.Assertions.*;

import com.comp5590.configuration.AppConfig;
import com.comp5590.managers.security.passwords.Argon2PasswordManager;
import com.comp5590.managers.security.passwords.BCryptPasswordManager;
import com.comp5590.managers.security.passwords.PasswordManager;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@Order(3)
public class PasswordTests extends SetupTests {

    @Test
    public void testPasswordInstanceOfArgon() {
        PasswordManager pm = PasswordManager.getInstanceOf(AppConfig.HASH_ALGORITHM);
        assertInstanceOf(Argon2PasswordManager.class, pm);
    }

    @Test
    public void testPasswordInstanceOfBcrypt() {
        PasswordManager pm = PasswordManager.getInstanceOf("BCrypt");
        assertInstanceOf(BCryptPasswordManager.class, pm);
    }

    @Test
    public void testPasswordThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> PasswordManager.getInstanceOf("INVALID PASSWORD MANAGER"));
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

    @Test
    public void testBCryptPasswordValid() {
        PasswordManager bCryptPasswordManager = new BCryptPasswordManager();
        bCryptPasswordManager.initialise();
        String password = "password";
        String hashedPassword = bCryptPasswordManager.hashPassword(password);
        assertTrue(bCryptPasswordManager.passwordMatches(hashedPassword, password));
    }

    @Test
    public void testBCryptPasswordInvalid() {
        PasswordManager bCryptPasswordManager = new BCryptPasswordManager();
        bCryptPasswordManager.initialise();
        String password = "password";
        String hashedPassword = bCryptPasswordManager.hashPassword(password);
        assertFalse(bCryptPasswordManager.passwordMatches(hashedPassword, "password1"));
    }
}
