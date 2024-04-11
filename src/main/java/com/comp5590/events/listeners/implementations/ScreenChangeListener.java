package com.comp5590.events.listeners.implementations;

import com.comp5590.events.eventtypes.screens.ScreenChangeEvent;
import com.comp5590.events.listeners.interfaces.ScreenListener;

public class ScreenChangeListener implements ScreenListener {

    @Override
    public ScreenChangeEvent onScreenChange(ScreenChangeEvent event) {
        eventLogger.debug("Screen change event: " + event.getScreen().getClass().getSimpleName());
        return event;
    }
}
