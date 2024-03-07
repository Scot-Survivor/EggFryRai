package com.comp5590.managers.security.passwords;

import com.comp5590.configuration.AppConfig;
import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordManager extends PasswordManager {

    private final int rounds = AppConfig.BCRYPT_ROUNDS;

    @Override
    public boolean passwordMatches(String storedPassword, String userPassword) {
        String hashed = decodeBase64(storedPassword);
        return BCrypt.checkpw(userPassword, hashed);
    }

    @Override
    public String hashPassword(String userPassword) {
        return encodeBase64(BCrypt.hashpw(userPassword, BCrypt.gensalt(rounds)));
    }

    @Override
    public void initialise() {
        available = true;
    }
}
