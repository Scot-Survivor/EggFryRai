package com.comp5590.events.listeners.interfaces;

import com.comp5590.events.eventtypes.database.*;

public interface DatabaseListener extends Listener {
    default DatabaseInteractionEvent onDatabaseInteraction(DatabaseInteractionEvent event) {
        return event;
    }

    default EntitySaveEvent onEntitySave(EntitySaveEvent event) {
        return event;
    }

    default EntityUpdateEvent onEntityUpdate(EntityUpdateEvent event) {
        return event;
    }

    default EntityDeleteEvent onEntityDelete(EntityDeleteEvent event) {
        return event;
    }

    default EntityQueryAllEvent onEntityQueryAll(EntityQueryAllEvent event) {
        return event;
    }

    default EntityQueryByPropertyEvent onEntityQueryByProperty(EntityQueryByPropertyEvent event) {
        return event;
    }

    default EntityQueryEvent onEntityQuery(EntityQueryEvent event) {
        return event;
    }

    default EntityQueryOneEvent onEntityQueryOne(EntityQueryOneEvent event) {
        return event;
    }
}
