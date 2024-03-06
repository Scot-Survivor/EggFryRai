package com.comp5590.managers;

import com.comp5590.configuration.AppConfig;
import jakarta.persistence.Entity;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import org.apache.logging.log4j.core.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.reflections.Reflections;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class DatabaseManager {
    public static DatabaseManager INSTANCE;

    // This is a getter method for the sessionFactory field
    @Getter
    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;
    private final Logger logger = LoggerManager.getInstance().getLogger(DatabaseManager.class);

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
        logger.debug("Using database properties file: " + AppConfig.DATABASE_PROPERTIES_FILE);
        File file = new File(AppConfig.DATABASE_PROPERTIES_FILE);
        if (!file.exists() || file.isDirectory()) {
            logger.fatal(AppConfig.DATABASE_PROPERTIES_FILE + " not found");
            throw new RuntimeException(AppConfig.DATABASE_PROPERTIES_FILE + " not found");
        }
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            logger.fatal("Failed to load " + AppConfig.DATABASE_PROPERTIES_FILE + " : " + e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
        }
        Configuration configuration = new Configuration();
        getEntityClasses().forEach(c -> {
            configuration.addAnnotatedClass(c);
            logger.debug("Added entity class: " + c.getName());

        });
        configuration.setProperties(properties);

        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    private List<Class<?>> getEntityClasses() {
        Reflections reflections = new Reflections("com.comp5590.entities");
        return reflections.getTypesAnnotatedWith(Entity.class).stream().toList();
    }

    public boolean testConnection() {
        try {
            sessionFactory.openSession();
            return true;
        } catch (Exception e) {
            logger.error("Failed to connect to database: " + e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    public boolean save(Object object) {
        try {
            logger.debug("Saving object: " + object.toString());
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(object);
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            logger.error("Failed to save object: " + e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    public int saveGetId(Object object) {
        try {
            logger.debug("Saving object: " + object.toString());
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            int id = (int) session.save(object);
            session.getTransaction().commit();
            session.close();
            return id;
        } catch (Exception e) {
            logger.error("Failed to save object: " + e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
            return -1;
        }
    }

    /**
     * Execute a query against the database return list of results
     * @param query Hibernate Query to execute
     * @return List of results
     */
    public List<?> query(String query) {
        try {
            logger.debug("Executing query: " + query);
            Session session = sessionFactory.openSession();
            TypedQuery<?> q = session.createQuery(query);
            List<?> results = q.getResultList();
            session.close();
            return results;
        } catch (Exception e) {
            logger.error("Failed to execute query: " + e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    public boolean update(Object object) {
        try {
            logger.debug("Updating object: " + object.toString());
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.merge(object);
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            logger.error("Failed to update object: " + e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    public <T> List<T> getAll(final Class<T> type) {
        final Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        // It's safe to use string append here as we are using a class object name
        final List<T> result = session.createQuery("from " + type.getName(), type).getResultList();
        session.getTransaction().commit();
        return result;
    }

    /**
     * Get an object from the database by its ID
     * @param type The type of object to get
     * @param id The ID of the object to get
     * @return The object
     * @param <T> The type of object to get
     */
    public <T> T get(final Class<T> type, final int id) {
        final Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        final T result = session.get(type, id);
        session.getTransaction().commit();
        return result;
    }

    /**
     * Get by a property
     * @param type The type of object to get
     * @param property The property to search by
     * @param value The value to search for
     * @return The object
     * @param <T> The type of object to get
     */
    public <T> T getByProperty(final Class<T> type, final String property, final Object value) {
        final Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        final T result = (T) session.createQuery("from " + type.getName() + " where " + property + " = :value")
                .setParameter("value", value).uniqueResult();
        session.getTransaction().commit();
        return result;
    }

    /**
     * Delete an object from the database
     */
    public boolean delete(Object object) {
        try {
            logger.debug("Deleting object: " + object.toString());
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.delete(object);
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            logger.error("Failed to delete object: " + e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }
}
