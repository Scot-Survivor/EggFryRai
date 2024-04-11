package com.comp5590.events.listeners.interfaces;

import com.comp5590.events.eventtypes.GenericEvent;
import com.comp5590.managers.LoggerManager;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.core.Logger;

// Inspired by: net.dv8tion.jda.api.hooks.ListenerAdapter.java
public interface Listener {
    Logger eventLogger = LoggerManager.getInstance().getLogger(Listener.class, "DEBUG");

    // Store the Event and Listener Class that was found
    HashMap<ImmutablePair<Class<?>, Class<?>>, MethodHandle> methodHandles = new HashMap<>();

    default GenericEvent onGenericEvent(GenericEvent event) {
        return event;
    }

    default GenericEvent onEvent(GenericEvent event) {
        onGenericEvent(event);
        Class<?> eventClass = event.getClass();
        // use reflections to find implementations of listeners, since getInterfaces(),
        // does not return anything
        for (Class<?> listenerClass : this.getClass().getInterfaces()) {
            // Iterate through the children of the Listener classes
            if (listenerClass != Listener.class) {
                ImmutablePair<Class<?>, Class<?>> pair = new ImmutablePair<>(eventClass, listenerClass);
                MethodHandle method = methodHandles.computeIfAbsent(pair, this::getMethod);
                if (method != null) {
                    try {
                        return (GenericEvent) method.invoke(this, event);
                    } catch (Throwable e) {
                        eventLogger.error(
                            "An error occurred while trying to invoke the method({}) for event: {}",
                            listenerClass.getSimpleName(),
                            e.getMessage()
                        );
                        eventLogger.debug(Arrays.toString(e.getStackTrace()));
                    }
                }
            }
        }
        return event;
    }

    /**
     * Get the method that should be called for the given event
     *
     * @param classPair ImmutablePair of (eventClass, listenerClass)
     * @return The method that should be called
     */
    private MethodHandle getMethod(ImmutablePair<Class<?>, Class<?>> classPair) {
        Class<?> eventClass = classPair.getKey();
        Class<?> listenerClass = classPair.getValue();
        String name = eventClass.getSimpleName();
        MethodType type = MethodType.methodType(eventClass, eventClass);
        try {
            name = "on" + name.substring(0, name.length() - "Event".length());
            return MethodHandles.lookup().findVirtual(listenerClass, name, type);
        } catch (NoSuchMethodException e) {
            // If no method is found for this listener that's fine, just go to next one.
            return null;
        } catch (IllegalAccessException e) {
            eventLogger.error("Method({}) is not accessible: {}", name, e.getMessage());
            eventLogger.debug(Arrays.toString(e.getStackTrace()));
            return null;
        } catch (Throwable e) {
            eventLogger.error(
                "An error occurred while trying to get the method({}) for event: {}",
                eventClass.getSimpleName(),
                e.getMessage()
            );
            eventLogger.debug(Arrays.toString(e.getStackTrace()));
            return null;
        }
    }
}
