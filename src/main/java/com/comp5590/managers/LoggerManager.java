package com.comp5590.managers;


import com.comp5590.App;
import com.comp5590.configuration.AppConfig;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import java.util.HashMap;

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

    private Appender getAppender(Class<?> clazz) {
        PatternLayout pattern = PatternLayout.newBuilder()
                .withDisableAnsi(false)
                .withPattern("%highlight{%d{HH:mm:ss.SSS} %level %msg%n}{FATAL=red blink, ERROR=red, WARN=yellow bold, INFO=black, DEBUG=green bold, TRACE=blue}")
                .build();
        return ConsoleAppender.createDefaultAppenderForLayout(pattern);
    }

    private void createLogger(Class<?> clazz) {
        createLogger(clazz, getLogLevel());
    }

    private void createLogger(Class<?> clazz, String log_level) {
        if (log_level == null || log_level.isEmpty()) {
            getLogger().error("Invalid log level given to createLogger");
            return;
        }

        Logger logger = (Logger) LogManager.getLogger(clazz);
        logger.getAppenders().values().forEach(logger::removeAppender); // remove all appender
        logger.addAppender(getAppender(clazz));
        logger.setLevel(Level.getLevel(log_level));
        loggers.put(clazz, logger);
    }


    public static LoggerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LoggerManager();
        }
        return INSTANCE;
    }

    public Logger getLogger() {
        return getLogger(App.class);
    }

    public Logger getLogger(Class<?> clazz) {
        if (loggers.containsKey(clazz)) return loggers.get(clazz);
        createLogger(clazz);
        return loggers.get(clazz);
    }

    public Logger getLogger(Class<?> clazz, String log_level) {
        if (loggers.containsKey(clazz)) return loggers.get(clazz);
        createLogger(clazz, log_level);
        return loggers.get(clazz);
    }
}
