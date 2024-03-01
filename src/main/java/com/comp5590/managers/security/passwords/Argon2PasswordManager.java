package com.comp5590.managers.security.passwords;

import com.comp5590.configuration.AppConfig;
import com.comp5590.managers.security.passwords.PasswordManager;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

/**
 * Argon2(i by default) Hasher class. As per:
 * <a href="https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html">OWASP</a>
 * This is currently the standard hashing algorithm, and most recommended.
 */
public class Argon2PasswordManager extends PasswordManager {
    private final AppConfig appConfig = AppConfig.getInstance();
    private final int saltLength = appConfig.HASH_SALT_LENGTH;
    private final int hashLength = appConfig.HASH_LENGTH;
    private final int parallelism = appConfig.HASH_PARALLELISM;
    private final int memoryInKb = appConfig.HASH_MEMORY;
    private final int iterations = appConfig.HASH_ITERATIONS;

    Argon2 argon2Factory;

    public Argon2PasswordManager() {
        argon2Factory = Argon2Factory.createAdvanced(saltLength, hashLength);
    }

    @Override
    public void initialise() {
        /* iterations = Argon2Helper.findIterations(argon2Factory, 1000,
                memoryInKb, parallelism); */  // Removed due to iterations being set in AppConfig
        available = true;
    }

    @Override
    public boolean passwordMatches(String storedPassword, String userPassword) {
        byte[] arr = userPassword.getBytes();
         boolean val = argon2Factory.verify(decodeBase64(storedPassword), arr);
         argon2Factory.wipeArray(arr);  // Securely wipe.
         return val;
    }

    @Override
    public String hashPassword(String userPassword) {
        return this.encodeBase64(
                argon2Factory.hash(iterations, memoryInKb, parallelism, userPassword.getBytes()
                ));
    }
}
