package com.comp5590.events.eventtypes.database;

import com.comp5590.App;
import com.comp5590.events.eventtypes.CancellableEvent;

public class EntityDeleteEvent extends CancellableEvent {

    public EntityDeleteEvent(Class<?> clazz, App app) {
        super("Deleting Entity: " + clazz.getSimpleName(), app, false);
    }

    public EntityDeleteEvent() {}
}
