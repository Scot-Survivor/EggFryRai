package com.comp5590.events.eventtypes.database;

import com.comp5590.App;

public class EntityUpdateEvent extends DatabaseInteractionEvent {

    public EntityUpdateEvent(Class<?> clazz, Object entity, App app) {
        super("Updating Entity: " + clazz.getSimpleName(), entity, app);
    }

    public EntityUpdateEvent() {}
}
