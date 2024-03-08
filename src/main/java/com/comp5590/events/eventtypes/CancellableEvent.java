package com.comp5590.events.eventtypes;

import com.comp5590.App;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancellableEvent extends GenericEvent {

    private boolean cancelled;

    public CancellableEvent(String message, App app, boolean cancelled) {
        super(message, app);
        this.cancelled = cancelled;
    }

    public CancellableEvent() {
        super();
        this.cancelled = false;
    }
}
