package com.comp5590.events.listeners.interfaces;

import com.comp5590.events.eventtypes.users.LoginEvent;
import com.comp5590.events.eventtypes.users.LogoutEvent;
import com.comp5590.events.eventtypes.users.UserUpdateEvent;

/**
 * Marker interface for user listeners
 */
public interface UserListener extends Listener {
    default LoginEvent onLogin(LoginEvent event) {
        return event;
    }

    default LogoutEvent onLogout(LogoutEvent event) {
        return event;
    }

    default UserUpdateEvent onUserUpdate(UserUpdateEvent event) {
        return event;
    }
}
