package com.comp5590.events.eventtypes;

import com.comp5590.App;
import com.comp5590.managers.LoggerManager;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.core.Logger;

/**
 * Generic event class that can be extended to create custom events.
 */
@Getter
public class GenericEvent {

    @Setter
    private String message;

    /**
     * Fields likely to be used by many events
     */
    private final App app;
    private final Logger logger;

    public GenericEvent(String message, App app) {
        this.message = message;
        this.app = app;
        this.logger = LoggerManager.getInstance().getLogger(this.getClass());
    }

    public GenericEvent() {
        this.message = "";
        this.app = null;
        this.logger = LoggerManager.getInstance().getLogger(this.getClass());
        this.logger.warn("Event was called with 0 args this should only be used for testing purposes.");
    }
}
