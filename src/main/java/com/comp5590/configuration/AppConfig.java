package com.comp5590.configuration;

import com.comp5590.managers.MasterLogger;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.logging.log4j.core.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * Application configuration
 * This class is a singleton
 * Any field to save must be all uppercase, public, and not final
 * Any field to save must be of type int, boolean, double, float, long, short, byte, char, String
 * Any field to save must be initialized
 * Any field to save must be non-static
 */
public class AppConfig {
    private final File configFile = new File(ConfigFile);

    Configuration config;
    private final Parameters params = new Parameters();

    public static final String APP_NAME = "Patient Doctor Management System";
    public static final String ConfigFile = "config.properties";
    private static AppConfig instance;
    private static final Logger logger = MasterLogger.getInstance().getLogger(AppConfig.class);

    // Configuration

    // Password hashing configuration
    public String HASH_ALGORITHM = "Argon2";
    public int HASH_ITERATIONS = 1;
    public int HASH_MEMORY = 10 * 1024; // 10 MB
    public int HASH_PARALLELISM = 1;
    public final int HASH_SALT_LENGTH = 128 / 8; // 128 bits
    public final int HASH_LENGTH = 256 / 8; // 256 bits

    public final String LOG_LEVEL = "DEBUG";


    // Methods
    private AppConfig() {
        setup();
    }

    private void setup() {
        boolean exists = configFile.exists() && !configFile.isDirectory();
        boolean created = false;
        if (!exists) {
            try {
                created = configFile.createNewFile();
            } catch (Exception e) {
                logger.error("Config File could not be created: " + e.getMessage());
                logger.debug(Arrays.toString(e.getStackTrace()));
            } finally {
                if (!created) {
                    logger.warn("Config file was not created, but no error throw");
                }
            }
        }
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder;
        try {
             builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                     .configure(params.fileBased()
                             .setFile(configFile));
            config = builder.getConfiguration();
        } catch (Exception e) {
            logger.error("File Configuration Object could not be created: " + e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
            return;
        }

        if (config == null) {
            logger.error("The config is null. This should not happen");
        }

        List<Field> fields = List.of(this.getClass().getDeclaredFields());
        if (exists) {
            // If exists then load the configuration
            for (Field field : fields) {
                if (!Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()) &&
                        !Modifier.isPrivate(field.getModifiers()) &&
                        field.getName().toUpperCase().equals(field.getName())) {
                    try {
                        Object val = config.getProperty(field.getName());
                        // Cast val to the type of field
                        val = ConvertUtils.convert(val, field.getType());
                        field.set(this, val);
                    } catch (IllegalAccessException e) {
                        logger.error("Error decoding a value in "+ ConfigFile + " config file: " + e.getMessage());
                        logger.debug(Arrays.toString(e.getStackTrace()));
                    }
                }
            }
        } else {
            // If not exists then create and save with all default values
            for (Field field : fields) {
                if (!Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()) &&
                        !Modifier.isPrivate(field.getModifiers()) &&
                        field.getName().toUpperCase().equals(field.getName())) {
                    try {
                        config.setProperty(field.getName(), field.get(this));
                    } catch (IllegalAccessException e) {
                        logger.error("Bad access modifier type on field(" + field.getName() + "): " + e.getMessage());
                        logger.debug(Arrays.toString(e.getStackTrace()));
                    }
                }
            }
            try {
                builder.save();
            } catch (Exception e) {
                logger.error("Failed to save the config " + ConfigFile + ": " + e.getMessage());
                logger.debug(Arrays.toString(e.getStackTrace()));
            }
        }
        builder.setAutoSave(true);
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }
}