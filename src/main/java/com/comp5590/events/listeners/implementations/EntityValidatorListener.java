package com.comp5590.events.listeners.implementations;

import com.comp5590.events.eventtypes.database.*;
import com.comp5590.events.listeners.interfaces.DatabaseListener;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.hibernate.validator.HibernateValidator;

/**
 * Listener to validate entities before they are saved to the database.
 * Using hibernate validators.
 */
public class EntityValidatorListener implements DatabaseListener {

    private Validator validator;

    public EntityValidatorListener() {
        this.validator =
        Validation.byProvider(HibernateValidator.class).configure().buildValidatorFactory().getValidator();
    }

    @Override
    public DatabaseInteractionEvent onDatabaseInteraction(DatabaseInteractionEvent event) {
        if (event.isCancelled()) {
            return event;
        }
        if (event.getEntity() == null) {
            // If entity is null we do not want to commit this.
            event.setCancelled(true);
            return event;
        }
        Set<ConstraintViolation<Object>> violations = validator.validate(event.getEntity());
        if (!violations.isEmpty()) {
            event.setCancelled(true);
            event
                .getLogger()
                .error(
                    "Entity failed validation: " +
                    event.getEntity().toString() +
                    " - " +
                    event.getEntity().getClass().getSimpleName()
                );
            // Debug log the violations
            for (ConstraintViolation<Object> violation : violations) {
                event.getLogger().debug("Entity Violation: " + violation.getMessage());
            }
            return event;
        }
        return event;
    }
}
