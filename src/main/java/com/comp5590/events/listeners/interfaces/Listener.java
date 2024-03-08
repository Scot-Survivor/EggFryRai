package com.comp5590.events.listeners.interfaces;

import com.comp5590.events.eventtypes.GenericEvent;
import com.comp5590.managers.LoggerManager;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import org.apache.logging.log4j.core.Logger;

// Inspired by: net.dv8tion.jda.api.hooks.ListenerAdapter.java
public interface Listener {
    Logger eventLogger = LoggerManager.getInstance().getLogger(Listener.class);

    default GenericEvent onGenericEvent(GenericEvent event) {
        return event;
    }

    default GenericEvent onEvent(GenericEvent event) {
        onGenericEvent(event);

        // use reflections to find implementations of listeners, since getInterfaces(),
        // does not return anything
        for (Class<?> clazz : this.getClass().getInterfaces()) {
            // Iterate through the children of the Listener classes
            if (clazz != Listener.class) {
                MethodHandle method = getMethod(event.getClass());
                if (method != null) {
                    try {
                        return (GenericEvent) method.invoke(this, event);
                    } catch (Throwable e) {
                        eventLogger.error(
                            "An error occurred while trying to invoke the method({}) for event: {}",
                            clazz.getSimpleName(),
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
     * @param clazz The class of the event
     * @return The method that should be called
     */
    private MethodHandle getMethod(Class<?> clazz) {
        String name = clazz.getSimpleName();
        MethodType type = MethodType.methodType(clazz, clazz);
        try {
            name = "on" + name.substring(0, name.length() - "Event".length());
            return MethodHandles.lookup().findVirtual(getClass(), name, type);
        } catch (NoSuchMethodException e) {
            eventLogger.error("No method found for event " + clazz.getSimpleName());
            eventLogger.debug(Arrays.toString(e.getStackTrace()));
            return null;
        } catch (IllegalAccessException e) {
            eventLogger.error("Method({}) is not accessible: {}", name, e.getMessage());
            eventLogger.debug(Arrays.toString(e.getStackTrace()));
            return null;
        } catch (Throwable e) {
            eventLogger.error(
                "An error occurred while trying to get the method({}) for event: {}",
                clazz.getSimpleName(),
                e.getMessage()
            );
            eventLogger.debug(Arrays.toString(e.getStackTrace()));
            return null;
        }
    }
}
