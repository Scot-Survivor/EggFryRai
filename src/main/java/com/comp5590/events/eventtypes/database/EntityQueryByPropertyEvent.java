package com.comp5590.events.eventtypes.database;

import com.comp5590.App;
import com.comp5590.events.eventtypes.CancellableEvent;

public class EntityQueryByPropertyEvent extends CancellableEvent {

    public EntityQueryByPropertyEvent(Class<?> clazz, String property, Object value, App app) {
        super("Querying Entity: " + clazz.getSimpleName() + " by " + property + " = " + value, app, false);
    }

    public EntityQueryByPropertyEvent() {}
}
