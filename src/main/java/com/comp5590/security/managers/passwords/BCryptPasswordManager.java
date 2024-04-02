package com.comp5590.security.managers.passwords;

import com.comp5590.configuration.AppConfig;
import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordManager extends PasswordManager {

    private final int rounds = AppConfig.BCRYPT_ROUNDS;

    /**
     * Check if the password provided by the user matches the stored password.
     * @param storedPassword The base64 encoded hash from the database.
     * @param userPassword The password provided from the user.
     * @return True if the passwords match, false otherwise.
     */
    @Override
    public boolean passwordMatches(String storedPassword, String userPassword) {
        String hashed = decodeBase64(storedPassword);
        return BCrypt.checkpw(userPassword, hashed);
    }

    /**
     * Hash the provided user password using BCrypt
     * @param userPassword the provided user password in plain text
     * @return the hashed password
     */
    @Override
    public String hashPassword(String userPassword) {
        return encodeBase64(BCrypt.hashpw(userPassword, BCrypt.gensalt(rounds)));
    }

    @Override
    public void initialise() {
        available = true;
    }
}
