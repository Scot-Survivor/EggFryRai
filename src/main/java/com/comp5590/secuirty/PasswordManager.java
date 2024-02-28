package com.comp5590.secuirty;

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

    // TODO: Write a factory function to be able to get the needed manager for a given hash. In the future
}
