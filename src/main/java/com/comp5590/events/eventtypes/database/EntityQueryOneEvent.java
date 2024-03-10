package com.comp5590.events.eventtypes.database;

import com.comp5590.App;

public class EntityQueryOneEvent extends DatabaseInteractionEvent {

    public EntityQueryOneEvent(Class<?> clazz, Object id, App app) {
        super("Querying Entity: " + clazz.getSimpleName() + " by id = " + id, app, null);
    }

    public EntityQueryOneEvent() {}
}
