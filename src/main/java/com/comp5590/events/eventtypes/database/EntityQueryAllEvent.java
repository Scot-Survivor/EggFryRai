package com.comp5590.events.eventtypes.database;

import com.comp5590.App;

public class EntityQueryAllEvent extends DatabaseInteractionEvent {

    public EntityQueryAllEvent(Class<?> clazz, App app) {
        super("Querying All Entities: " + clazz.getSimpleName(), null, app);
    }

    public EntityQueryAllEvent() {}
}
