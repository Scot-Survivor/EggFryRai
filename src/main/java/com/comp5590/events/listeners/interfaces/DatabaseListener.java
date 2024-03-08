package com.comp5590.events.listeners.interfaces;

import com.comp5590.events.eventtypes.database.EntityDeleteEvent;
import com.comp5590.events.eventtypes.database.EntitySaveEvent;
import com.comp5590.events.eventtypes.database.EntityUpdateEvent;

public interface DatabaseListener extends Listener {
    default EntitySaveEvent onEntitySave(EntitySaveEvent event) {
        return event;
    }

    default EntityUpdateEvent onEntityUpdate(EntityUpdateEvent event) {
        return event;
    }

    default EntityDeleteEvent onEntityDelete(EntityDeleteEvent event) {
        return event;
    }
}
