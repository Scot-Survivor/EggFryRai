package com.comp5590.secuirty;

public abstract class PasswordManager {
    /**
     * Checks if two hashes are identical
     * @param storedPassword The base64 encoded hash from the database.
     * @param userPassword The password provided from the user.
     * @return Whether they match
     */
    abstract boolean passwordMatches(String storedPassword, String userPassword);

    /**
     * This hashes a password and returns a base64 encoded string
     * @param userPassword the provided user password in plain text
     * @return base64 encoded password
     */
    abstract String hashPassword(String userPassword);

    // TODO: Write a factory function to be able to get the needed manager for a given hash. In the future
}
