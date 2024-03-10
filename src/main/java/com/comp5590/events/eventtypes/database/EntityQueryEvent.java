package com.comp5590.events.eventtypes.database;

import com.comp5590.App;

public class EntityQueryEvent extends DatabaseInteractionEvent {

    public EntityQueryEvent(String query, App app) {
        super("Querying Database with query: " + query, null, app);
    }

    public EntityQueryEvent() {}
}
