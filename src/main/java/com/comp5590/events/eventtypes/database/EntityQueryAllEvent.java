package com.comp5590.events.eventtypes.database;

import com.comp5590.App;
import com.comp5590.events.eventtypes.CancellableEvent;

public class EntityQueryAllEvent extends CancellableEvent {

    public EntityQueryAllEvent(Class<?> clazz, App app) {
        super("Querying All Entities: " + clazz.getSimpleName(), null, false);
    }

    public EntityQueryAllEvent() {}
}
