package com.comp5590.managers.secuirty.passwords;

import com.comp5590.managers.LoggerManager;

import java.nio.charset.StandardCharsets;

public abstract class PasswordManager {
    protected boolean available = false;
    /**
     * Checks if two hashes are identical
     * @param storedPassword The base64 encoded hash from the database.
     * @param userPassword The password provided from the user.
     * @return Whether they match
     */
    public abstract boolean passwordMatches(String storedPassword, String userPassword);

    /**
     * This hashes a password and returns a base64 encoded string
     * @param userPassword the provided user password in plain text
     * @return base64 encoded password
     */
    public abstract String hashPassword(String userPassword);

    /**
     * For heavy computation, this method should be called to initialise the password manager.
     */
    public abstract void initialise();

    /**
     * Check if the password manager is available
     * @return true if available, false otherwise
     */
    public boolean isAvailable() {
        return available;
    }

    public static PasswordManager getInstanceOf(String passwordManager) {
        LoggerManager.getInstance().getLogger(PasswordManager.class)
                .debug("Getting instance of password manager: " + passwordManager);
        if (Argon2PasswordManager.class.getName().contains(passwordManager)) {
            return new Argon2PasswordManager();
        }
        return null;
    }

    public String encodeBase64(String data) {
        return java.util.Base64.getEncoder().encodeToString(data.getBytes());
    }

    public String decodeBase64(String data) {
        return new String(java.util.Base64.getDecoder().decode(data), StandardCharsets.UTF_8);
    }
}
