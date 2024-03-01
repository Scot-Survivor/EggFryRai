package com.comp5590.configuration;

import com.comp5590.managers.LoggerManager;
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
import java.util.Arrays;
import java.util.Iterator;

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

    private Configuration config;

    private FileBasedConfigurationBuilder<FileBasedConfiguration> builder;
    private final Parameters params = new Parameters();

    public static final String APP_NAME = "Patient Doctor Management System";
    public static String ConfigFile = "config.properties";
    private static AppConfig instance;

    // Hard code a DEBUG logger due to the fact that the configuration level can't load till this is complete.
    private static final Logger logger = LoggerManager.getInstance().getLogger(AppConfig.class, "DEBUG");

    // Configuration

    // Password hashing configuration
    public String HASH_ALGORITHM = "Argon2";
    public int HASH_ITERATIONS = 1;
    public int HASH_MEMORY = 10 * 1024; // 10 MB
    public int HASH_PARALLELISM = 1;
    public final int HASH_SALT_LENGTH = 128 / 8; // 128 bits
    public final int HASH_LENGTH = 256 / 8; // 256 bits

    public String LOG_LEVEL = "DEBUG";


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

        try {
            builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                    .configure(params.fileBased().setFile(configFile));
            config = builder.getConfiguration();
            // logger debug output all the properties
            logger.debug("Loaded configuration: ");
            for (Iterator<String> it = config.getKeys(); it.hasNext(); ) {
                String key = it.next();
                logger.debug(key + " = " + config.getProperty(key));
            }
            // Get list of AppConfig fields
            Field[] fields = this.getClass().getDeclaredFields();
            // Load all fields from the config file
            for (Field field : fields) {
                if (!Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()) &&
                        !Modifier.isPrivate(field.getModifiers()) &&
                        field.getName().toUpperCase().equals(field.getName())) {
                    try {
                        if (config.containsKey(field.getName())) {
                            field.set(this, ConvertUtils.convert(config.getProperty(field.getName()), field.getType()));
                        } else {
                            config.addProperty(field.getName(), field.get(this));
                            logger.debug("Added property to AppConfig: " + field.getName() + " = " + field.get(this));
                        }
                    } catch (IllegalAccessException e) {
                        logger.error("Bad access modifier type on field(" + field.getName() + "): " + e.getMessage());
                        logger.debug(Arrays.toString(e.getStackTrace()));
                    }
                }
            }
            try {
                builder.save();
            } catch (Exception e) {
                logger.error("Failed to save configuration: " + e.getMessage());
                logger.debug(Arrays.toString(e.getStackTrace()));
            }
        } catch (Exception e) {
            logger.error("Failed to load configuration: " + e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
        }
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }
}
