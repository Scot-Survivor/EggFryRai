package com.comp5590.managers;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseManager {
    public static DatabaseManager INSTANCE;
    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;

    private DatabaseManager() {
        load();
    }

    public static DatabaseManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseManager();
        }
        return INSTANCE;
    }

    private void load() {
        Properties properties = new Properties();
        // Check hibernate.properties is found
        File file = new File("hibernate.properties");
        if (!file.exists() || file.isDirectory()) {
            // TODO: Change this to a logger
            throw new RuntimeException("hibernate.properties not found");
        }
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Configuration configuration = new Configuration();
        configuration.setProperties(properties);

        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    /**
     * Get the session factory
     * @return the session factory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
