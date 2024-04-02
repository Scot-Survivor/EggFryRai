package com.comp5590.managers;

import com.comp5590.App;
import com.comp5590.configuration.AppConfig;
import java.util.HashMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 * Different parts of the program, should have different dedicated loggers I.E Errors on GUI should be AppLogger
 * but authentication issues should be handled by PasswordManager or similar.
 * This Singleton class makes this painless.
 */
public class LoggerManager {

    private static LoggerManager INSTANCE;
    private final HashMap<Class<?>, Logger> loggers;

    private AppConfig config;

    private LoggerManager() {
        this.loggers = new HashMap<>();
    }

    /**
     * Get the log level from the config file
     * @return the log level
     */
    private String getLogLevel() {
        if (config == null) {
            config = AppConfig.getInstance();
        }
        if (config.LOG_LEVEL == null || config.LOG_LEVEL.isEmpty()) {
            config.LOG_LEVEL = "ERROR";
            return "ERROR";
        } else {
            return config.LOG_LEVEL;
        }
    }

    /**
     * Appender is a Log4J object that defines the output of the logger (I.E Console, File, etc)
     * @param clazz The class that the logger is for
     * @return The appender
     */
    private Appender getAppender(Class<?> clazz) {
        PatternLayout pattern = PatternLayout
            .newBuilder()
            .withDisableAnsi(false)
            .withPattern(
                "%highlight{[%d{HH:mm:ss.SSS} %level | " +
                clazz.getSimpleName() +
                "] %msg%n}{FATAL=red blink, ERROR=red, WARN=yellow bold, INFO=black, DEBUG=green bold, TRACE=blue}"
            )
            .build();
        return ConsoleAppender.createDefaultAppenderForLayout(pattern);
    }

    /**
     * Create a logger for a class
     * @param clazz The class that the logger is for
     */
    private void createLogger(Class<?> clazz) {
        createLogger(clazz, getLogLevel());
    }

    /**
     * Create a logger for a class with a specific log level
     * @param clazz The class that the logger is for
     * @param log_level The log level
     */
    private void createLogger(Class<?> clazz, String log_level) {
        if (log_level == null || log_level.isEmpty()) {
            getLogger().error("Invalid log level given to createLogger");
            return;
        }

        Logger logger = (Logger) LogManager.getLogger(clazz);
        logger.getAppenders().values().forEach(logger::removeAppender); // remove all appender
        ConsoleAppender appender = (ConsoleAppender) getAppender(clazz);
        appender.start(); // Since we're creating the appender manually, we need to start it manually
        logger.addAppender(appender);
        logger.setLevel(Level.getLevel(log_level));
        loggers.put(clazz, logger);
    }

    /**
     * Singleton instance of the LoggerManager
     * @return The instance
     */
    public static LoggerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LoggerManager();
        }
        return INSTANCE;
    }

    /**
     * Get a default logger
     * @return The logger
     */
    public Logger getLogger() {
        return getLogger(App.class);
    }

    /**
     * Get a logger for a specific class
     * @param clazz The class that the logger is for
     * @return The logger
     */
    public Logger getLogger(Class<?> clazz) {
        if (loggers.containsKey(clazz)) return loggers.get(clazz);
        createLogger(clazz);
        return loggers.get(clazz);
    }

    /**
     * Get a logger for a specific class with a specific log level
     * @param clazz The class that the logger is for
     * @param log_level The log level
     * @return The logger
     */
    public Logger getLogger(Class<?> clazz, String log_level) {
        if (loggers.containsKey(clazz)) return loggers.get(clazz);
        createLogger(clazz, log_level);
        return loggers.get(clazz);
    }
}
