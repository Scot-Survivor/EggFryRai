package com.comp5590.events.eventtypes.database;

import com.comp5590.App;
import com.comp5590.events.eventtypes.CancellableEvent;

public class EntityQueryEvent extends CancellableEvent {

    public EntityQueryEvent(String query, App app) {
        super("Querying Database with query: " + query, app, false);
    }

    public EntityQueryEvent() {}
}
