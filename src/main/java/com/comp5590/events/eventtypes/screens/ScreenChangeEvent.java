package com.comp5590.events.eventtypes.screens;

import com.comp5590.App;
import com.comp5590.events.eventtypes.CancellableEvent;
import com.comp5590.screens.misc.AbstractScreen;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScreenChangeEvent extends CancellableEvent {

    private AbstractScreen screen;
    private boolean hasChanged = false;

    public ScreenChangeEvent(AbstractScreen screen, App app) {
        super("Changing Screen to: " + screen.getClass().getSimpleName(), app, false);
        this.screen = screen;
    }

    public ScreenChangeEvent() {
        super();
        this.screen = null;
    }
}
