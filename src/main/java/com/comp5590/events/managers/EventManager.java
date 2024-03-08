package com.comp5590.events.managers;

import com.comp5590.events.eventtypes.GenericEvent;
import com.comp5590.events.listeners.interfaces.Listener;
import java.util.HashSet;
import java.util.Set;

/**
 * This class acts as a the initiator in the "observable" pattern.
 * It is responsible for managing the listeners and notifying them when an event occurs.
 */
public class EventManager {

    private static EventManager instance;
    private final Set<Listener> listeners;

    public EventManager(Set<Listener> listeners) {
        this.listeners = listeners;
    }

    public EventManager() {
        this.listeners = new HashSet<>();
    }

    public void addListener(Listener listener) {
        if (listeners.contains(listener)) {
            throw new IllegalArgumentException("Listener already registered");
        }
        listeners.add(listener);
    }

    public <T extends GenericEvent> T callEvent(T event) {
        for (Listener listener : listeners) {
            GenericEvent temp = listener.onEvent(event);
            // Check cast
            if (temp != null && temp.getClass().isInstance(event)) {
                event = (T) temp;
            } else {
                throw new IllegalArgumentException(
                    "Listener(%s) returned an event of the wrong type".formatted(listener.getClass().getSimpleName())
                );
            }
        }
        return event;
    }

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }
}
