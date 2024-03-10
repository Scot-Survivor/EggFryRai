package com.comp5590.events.eventtypes.database;

import com.comp5590.App;

public class EntityDeleteEvent extends DatabaseInteractionEvent {

    public EntityDeleteEvent(Class<?> clazz, Object entity, App app) {
        super("Deleting Entity: " + clazz.getSimpleName(), entity, app);
    }

    public EntityDeleteEvent() {}
}
