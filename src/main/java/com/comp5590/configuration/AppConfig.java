package com.comp5590.configuration;

import com.comp5590.managers.LoggerManager;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;
import org.apache.logging.log4j.core.Logger;

/**
 * Application configuration
 * This class is a singleton
 * Any field to save must be all uppercase, public, and not final
 * Any field to save must be of type int, boolean, double, float, long, short, byte, char, String
 * Any field to save must be initialized
 * Any field to save must be static
 */
public class AppConfig {

    private final File configFile = new File(ConfigFile);

    private Configuration config;

    private FileBasedConfigurationBuilder<FileBasedConfiguration> builder;
    private final Parameters params = new Parameters();

    public static final String APP_NAME = "User Doctor Management System";
    public static String ConfigFile = "config.properties";
    private static AppConfig instance;

    // Hard code a DEBUG logger due to the fact that the configuration level can't load till this is complete.
    private static final Logger logger = LoggerManager.getInstance().getLogger(AppConfig.class, "DEBUG");

    // Configuration

    // Password hashing configuration
    public static String HASH_ALGORITHM = "Argon2";
    public static int HASH_ITERATIONS = 1;
    public static int BCRYPT_ROUNDS = 11;
    public static int HASH_MEMORY = 10 * 1024; // 10 MB
    public static int HASH_PARALLELISM = 1;
    public static final int HASH_SALT_LENGTH = 128 / 8; // 128 bits
    public static final int HASH_LENGTH = 256 / 8; // 256 bits

    // 2FA Settings
    public static String TOTP_ISSUER_NAME = "User Doctor Management System";
    public static String TOTP_ALGORITHM = "SHA256";
    public static int TOTP_SECRET_CHARACTERS = 32; // Length of secret
    public static int TOTP_DIGITS = 6;
    public static int TOTP_TIME_PERIOD = 30;
    public static int TOTP_CODE_ROLL = 1; // Number of codes to check either side of the current time

    // General App Configuration
    public static String LOG_LEVEL = "DEBUG";

    // Database Configuration
    public static String DATABASE_PROPERTIES_FILE = "hibernate.properties";

    // Methods
    private AppConfig() {
        setup();
    }

    /**
     * Clean the file of any properties that are not in the AppConfig class
     * @return list of names of properties removed
     */
    public List<String> cleanConfig() {
        logger.warn("Cleaning configuration");
        List<String> removed = new ArrayList<>();
        // Load a new config from file
        Configurations configs = new Configurations();
        File configFile = new File(ConfigFile);
        PropertiesConfiguration propertiesConfig;
        try {
            propertiesConfig = configs.properties(configFile);
        } catch (ConfigurationException e) {
            logger.error("Failed to load configuration for cleaning: " + e.getMessage());
            return removed;
        }
        // Load all fields from the config file
        Iterator<String> itProperties = propertiesConfig.getKeys();
        while (itProperties.hasNext()) {
            String key = itProperties.next();
            if (!config.containsKey(key)) {
                propertiesConfig.clearProperty(key);
                removed.add(key);
            }
        }
        FileHandler handler = new FileHandler(propertiesConfig);
        try {
            handler.save(configFile);
        } catch (ConfigurationException e) {
            logger.error("Failed to save configuration after cleaning: " + e.getMessage());
        }
        return removed;
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
            builder =
            new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                .configure(params.fileBased().setFile(configFile));
            config = builder.getConfiguration();
            // logger debug output all the properties
            logger.debug("Loaded configuration: ");
            for (Iterator<String> it = config.getKeys(); it.hasNext();) {
                String key = it.next();
                logger.debug(key + " = " + config.getProperty(key));
            }
            // Get list of AppConfig fields
            List<Field> fields = getConfigFields();
            // Load all fields from the config file
            for (Field field : fields) {
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

    private boolean isValidField(Field field) {
        return (
            !Modifier.isFinal(field.getModifiers()) &&
            Modifier.isStatic(field.getModifiers()) &&
            !Modifier.isPrivate(field.getModifiers()) &&
            // Check the field is all uppercase
            field.getName().toUpperCase().equals(field.getName())
        );
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public void reload() {
        logger.warn("Reloading configuration");
        instance = new AppConfig(); // Force reload
    }

    public List<Field> getConfigFields() {
        return Arrays.stream(this.getClass().getDeclaredFields()).filter(this::isValidField).toList();
    }

    public void save() {
        try {
            builder.save();
        } catch (Exception e) {
            logger.error("Failed to save configuration: " + e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Get a value
     * @param key The key of the field
     */
    public Object getValue(String key) {
        return config.getProperty(key);
    }

    /**
     * Set a value without saving.
     * @param key The key of the field
     * @param value The value to set
     */
    public void setValue(String key, Object value) {
        setValue(key, value, false);
    }

    /**
     * Set a value and optionally save
     * @param key The key of the field
     * @param value The value to set
     * @param save Whether to save the configuration
     */
    public void setValue(String key, Object value, boolean save) {
        try {
            Field field = this.getClass().getDeclaredField(key);
            field.set(this, value);
            config.setProperty(key, value);
            if (save) {
                save();
            }
        } catch (Exception e) {
            logger.error("Failed to set value: " + e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
        }
    }
}
