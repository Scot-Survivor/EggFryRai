package com.comp5590.events.listeners.interfaces;

import com.comp5590.events.eventtypes.screens.ScreenChangeEvent;

public interface ScreenListener extends Listener {
    default ScreenChangeEvent onScreenChange(ScreenChangeEvent event) {
        return event;
    }
}
