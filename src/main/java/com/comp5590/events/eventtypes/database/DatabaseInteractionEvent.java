package com.comp5590.events.eventtypes.database;

import com.comp5590.App;
import com.comp5590.events.eventtypes.CancellableEvent;
import lombok.Getter;

@Getter
public class DatabaseInteractionEvent extends CancellableEvent {

    private Object entity;

    public DatabaseInteractionEvent(String message, Object entity, App app) {
        super(message, app, false);
        this.entity = entity;
    }

    public DatabaseInteractionEvent() {
        super();
        this.entity = null;
    }
}
