package com.comp5590.managers;


import com.comp5590.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;

/**
 * Different parts of the program, should have different dedicated loggers I.E Errors on GUI should be AppLogger
 * but authentication issues should be handled by PasswordManager or similar.
 * This Singleton class makes this painless.
 */
public class MasterLogger {
    private static MasterLogger INSTANCE;
    private HashMap<Class<?>, Logger> loggers;


    private MasterLogger() {
        this.loggers = new HashMap<>();
    }

    private void createLogger(Class<?> clazz) {
        loggers.put(clazz, LoggerFactory.getLogger(clazz));
    }


    public static MasterLogger getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MasterLogger();
        }
        return INSTANCE;
    }

    public Logger getLogger() {
        return loggers.get(MasterLogger.class);
    }

    public Logger getLogger(Class<?> clazz) {
        if (loggers.containsKey(clazz)) return loggers.get(clazz);
        createLogger(clazz);
        return loggers.get(clazz);
    }
}
