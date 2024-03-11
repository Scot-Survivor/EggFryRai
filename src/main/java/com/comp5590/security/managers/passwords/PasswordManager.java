package com.comp5590.security.managers.passwords;

import com.comp5590.managers.LoggerManager;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;
import org.apache.logging.log4j.core.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public abstract class PasswordManager {

    private static final Logger logger = LoggerManager.getInstance().getLogger(PasswordManager.class);
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
        Reflections reflections = new Reflections("com.comp5590.security.managers.passwords", Scanners.SubTypes);
        Set<Class<? extends PasswordManager>> pms = reflections.getSubTypesOf(PasswordManager.class);
        pms.forEach(pm -> {
            logger.debug("Found PasswordManager: " + pm.getSimpleName());
        });
        for (Class<? extends PasswordManager> pm : pms) {
            try {
                if (pm.getSimpleName().contains(passwordManager)) {
                    return pm.getConstructor().newInstance();
                }
            } catch (Exception e) {
                logger.error("Valid to load PasswordManager instance: " + e.getMessage());
                logger.debug(Arrays.toString(e.getStackTrace()));
            }
        }
        logger.warn("No password manager found for name: " + passwordManager);
        throw new IllegalArgumentException("No password manager found for name: " + passwordManager);
    }

    public String encodeBase64(String data) {
        return java.util.Base64.getEncoder().encodeToString(data.getBytes());
    }

    public String decodeBase64(String data) {
        return new String(java.util.Base64.getDecoder().decode(data), StandardCharsets.UTF_8);
    }
}
