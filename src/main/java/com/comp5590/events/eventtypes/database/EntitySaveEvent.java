package com.comp5590.events.eventtypes.database;

import com.comp5590.App;

public class EntitySaveEvent extends DatabaseInteractionEvent {

    public EntitySaveEvent(Class<?> clazz, Object entity, App app) {
        super("Saving Entity: " + clazz.getSimpleName(), entity, app);
    }

    public EntitySaveEvent() {}
}
