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
public class MasterLogger {
    private static MasterLogger INSTANCE;
    private HashMap<Class<?>, Logger> loggers;

    private AppConfig config = AppConfig.getInstance();


    private MasterLogger() {
        this.loggers = new HashMap<>();

    }

    private Appender getAppender(Class<?> clazz) {
        String PATTERN = "%d [%p|%c|%C{1}] %m%n";
        PatternLayout pattern = PatternLayout.newBuilder().withPattern(PATTERN).build();
        return ConsoleAppender.createDefaultAppenderForLayout(pattern);
    }

    private void createLogger(Class<?> clazz) {
        Logger logger = (Logger) LogManager.getLogger(clazz);
        logger.addAppender(getAppender(clazz));
        logger.setLevel(Level.getLevel(config.LOG_LEVEL));
        loggers.put(clazz, logger);
    }


    public static MasterLogger getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MasterLogger();
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
}
