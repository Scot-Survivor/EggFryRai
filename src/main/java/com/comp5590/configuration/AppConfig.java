package com.comp5590.configuration;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
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
    private FileBasedConfigurationBuilder<FileBasedConfiguration> builder;

    public static final String APP_NAME = "Patient Doctor Management System";
    public static final String ConfigFile = "config.properties";
    private static AppConfig instance;

    // Configuration

    // Password hashing configuration
    public String HASH_ALGORITHM = "Argon2";
    public int HASH_ITERATIONS = 1;
    public int HASH_MEMORY = 10 * 1024; // 10 MB
    public int HASH_PARALLELISM = 1;
    public final int HASH_SALT_LENGTH = 128 / 8; // 128 bits
    public final int HASH_LENGTH = 256 / 8; // 256 bits


    // Methods
    private AppConfig() {
        setup();
    }

    private void setup() {
        boolean exists = configFile.exists() && !configFile.isDirectory();
        if (!exists) {
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException(e);  // TODO: Change this to a logger
            }
        }
        try {
             builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                     .configure(params.fileBased()
                             .setFile(configFile));
            config = builder.getConfiguration();
        } catch (Exception e) {
            throw new RuntimeException(e);  // TODO: Change this to a logger
        }

        if (config == null) {
            throw new RuntimeException("Configuration is null");
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
                        Type type = field.getType();
                        if (type.equals(int.class)) {
                            val = Integer.parseInt((String) val);
                        } else if (type.equals(boolean.class)) {
                            val = Boolean.parseBoolean((String) val);
                        } else if (type.equals(double.class)) {
                            val = Double.parseDouble((String) val);
                        } else if (type.equals(float.class)) {
                            val = Float.parseFloat((String) val);
                        } else if (type.equals(long.class)) {
                            val = Long.parseLong((String) val);
                        } else if (type.equals(short.class)) {
                            val = Short.parseShort((String) val);
                        } else if (type.equals(byte.class)) {
                            val = Byte.parseByte((String) val);
                        } else if (type.equals(char.class)) {
                            val = ((String) val).charAt(0);
                        }
                        field.set(this, val);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);  // TODO: Change this to a logger
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
                        throw new RuntimeException(e);  // TODO: Change this to a logger
                    }
                }
            }
            try {
                builder.save();
            } catch (Exception e) {
                throw new RuntimeException(e);  // TODO: Change this to a logger
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
