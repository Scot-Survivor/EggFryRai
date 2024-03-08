package com.comp5590.events.eventtypes.database;

import com.comp5590.App;
import com.comp5590.events.eventtypes.CancellableEvent;

public class EntitySaveEvent extends CancellableEvent {

    public EntitySaveEvent(Class<?> clazz, App app) {
        super("Saving Entity: " + clazz.getSimpleName(), app, false);
    }

    public EntitySaveEvent() {}
}
