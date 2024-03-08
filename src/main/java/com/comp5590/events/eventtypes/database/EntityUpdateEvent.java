package com.comp5590.events.eventtypes.database;

import com.comp5590.App;
import com.comp5590.events.eventtypes.CancellableEvent;

public class EntityUpdateEvent extends CancellableEvent {

    public EntityUpdateEvent(Class<?> clazz, App app) {
        super("Updating Entity: " + clazz.getSimpleName(), app, false);
    }

    public EntityUpdateEvent() {}
}
