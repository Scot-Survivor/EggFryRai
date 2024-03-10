package com.comp5590.events.eventtypes.database;

import com.comp5590.App;
import com.comp5590.events.eventtypes.CancellableEvent;

public class EntityQueryOneEvent extends CancellableEvent {

    public EntityQueryOneEvent(Class<?> clazz, Object id, App app) {
        super("Querying Entity: " + clazz.getSimpleName() + " by id = " + id, app, false);
    }

    public EntityQueryOneEvent() {}
}
