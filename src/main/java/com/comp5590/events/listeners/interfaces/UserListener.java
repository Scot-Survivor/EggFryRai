package com.comp5590.events.listeners.interfaces;

import com.comp5590.events.eventtypes.users.LoginEvent;
import com.comp5590.events.eventtypes.users.LogoutEvent;

/**
 * Marker interface for user listeners
 */
public interface UserListener extends Listener {
    default LoginEvent onLogin(LoginEvent event) {
        return event;
    }

    default LoginEvent onLogout(LogoutEvent event) {
        return event;
    }
}
