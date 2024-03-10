package com.comp5590.events.eventtypes.database;

import com.comp5590.App;

public class EntityQueryByPropertyEvent extends DatabaseInteractionEvent {

    public EntityQueryByPropertyEvent(Class<?> clazz, String property, Object value, App app) {
        super("Querying Entity: " + clazz.getSimpleName() + " by " + property + " = " + value, null, app);
    }

    public EntityQueryByPropertyEvent() {}
}
