package com.comp5590.managers.secuirty;

import com.comp5590.configuration.AppConfig;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Helper;

import java.util.Arrays;
import java.util.Base64;

/**
 * Argon2(i by default) Hasher class. As per:
 * <a href="https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html">OWASP</a>
 * This is currently the standard hashing algorithm, and most recommended.
 */
public class ArgonPasswordManager extends PasswordManager {
    private final AppConfig appConfig = AppConfig.getInstance();
    private final int saltLength = appConfig.HASH_SALT_LENGTH;
    private final int hashLength = appConfig.HASH_LENGTH;
    private final int parallelism = appConfig.HASH_PARALLELISM;
    private final int memoryInKb = appConfig.HASH_MEMORY;
    private int iterations = appConfig.HASH_ITERATIONS;

    Argon2 argon2Factory;

    public ArgonPasswordManager() {
        argon2Factory = Argon2Factory.createAdvanced(saltLength, hashLength);
    }

    @Override
    public void initialise() {
        iterations = Argon2Helper.findIterations(argon2Factory, 1000,
                memoryInKb, parallelism);
        available = true;
    }

    @Override
    public boolean passwordMatches(String storedPassword, String userPassword) {
        byte[] arr = userPassword.getBytes();
         boolean val = argon2Factory.verify(Arrays.toString(Base64.getDecoder().decode(storedPassword)),
                 arr);
         argon2Factory.wipeArray(arr);  // Securely wipe.
         return val;
    }

    @Override
    public String hashPassword(String userPassword) {
        return Base64.getEncoder().encodeToString(
                argon2Factory.hash(iterations, memoryInKb, parallelism, userPassword.getBytes())
                        .getBytes());
    }
}
