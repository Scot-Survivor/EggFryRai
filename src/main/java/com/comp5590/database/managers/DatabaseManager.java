package com.comp5590.database.managers;

import com.comp5590.App;
import com.comp5590.configuration.AppConfig;
import com.comp5590.events.eventtypes.CancellableEvent;
import com.comp5590.events.eventtypes.database.*;
import com.comp5590.events.managers.EventManager;
import com.comp5590.managers.LoggerManager;
import jakarta.persistence.Entity;
import jakarta.persistence.TypedQuery;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import lombok.Getter;
import org.apache.logging.log4j.core.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.reflections.Reflections;

public class DatabaseManager {

    public static DatabaseManager INSTANCE;

    // This is a getter method for the sessionFactory field
    @Getter
    private SessionFactory sessionFactory;

    private ServiceRegistry serviceRegistry;
    private ValidatorFactory validatorFactory;
    private final Logger logger = LoggerManager.getInstance().getLogger(DatabaseManager.class);
    private EventManager eventManager;
    private App app;

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
        getEntityClasses()
            .forEach(c -> {
                configuration.addAnnotatedClass(c);
                logger.debug("Added entity class: " + c.getName());
            });
        configuration.setProperties(properties);

        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        validatorFactory = Validation.buildDefaultValidatorFactory();
        eventManager = EventManager.getInstance();
        app = App.getInstance();
    }

    private List<Class<?>> getEntityClasses() {
        Reflections reflections = new Reflections("com.comp5590.database.entities");
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

    public boolean validate(Object object) {
        return validatorFactory.getValidator().validate(object).isEmpty();
    }

    public Set<ConstraintViolation<Object>> validateWithViolations(Object object) {
        return validatorFactory.getValidator().validate(object);
    }

    private boolean shouldCancel(CancellableEvent event) {
        return event.isCancelled();
    }

    public boolean save(Object object) {
        if (shouldCancel(eventManager.callEvent(new EntitySaveEvent(object.getClass(), object, app)))) {
            logger.debug("Save event cancelled");
            return false;
        }
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
        if (shouldCancel(eventManager.callEvent(new EntitySaveEvent(object.getClass(), object, app)))) {
            logger.debug("Save event cancelled");
            return -1;
        }
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
        if (shouldCancel(eventManager.callEvent(new EntityQueryEvent(query, app)))) {
            logger.debug("Query event cancelled");
            return null;
        }
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
        if (shouldCancel(eventManager.callEvent(new EntityUpdateEvent(object.getClass(), object, app)))) {
            logger.debug("Update event cancelled");
            return false;
        }
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
        if (shouldCancel(eventManager.callEvent(new EntityQueryAllEvent(type, app)))) {
            logger.debug("Get all event cancelled");
            return null;
        }
        final Session session = sessionFactory.openSession();
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
        if (shouldCancel(eventManager.callEvent(new EntityQueryOneEvent(type, id, app)))) {
            logger.debug("Get event cancelled");
            return null;
        }
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        final T result = session.get(type, id);
        session.getTransaction().commit();
        session.close();
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
        if (shouldCancel(eventManager.callEvent(new EntityQueryByPropertyEvent(type, property, value, app)))) {
            logger.debug("Get by property event cancelled");
            return null;
        }
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        final T result = (T) session
            .createQuery("from " + type.getName() + " where " + property + " = :value")
            .setParameter("value", value)
            .uniqueResult();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    /**
     * Delete an object from the database
     */
    public boolean delete(Object object) {
        if (shouldCancel(eventManager.callEvent(new EntityDeleteEvent(object.getClass(), object, app)))) {
            logger.debug("Delete event cancelled");
            return false;
        }
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
