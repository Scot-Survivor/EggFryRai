package com.comp5590.events.listeners.implementations;

import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.events.eventtypes.users.UserUpdateEvent;
import com.comp5590.events.listeners.interfaces.UserListener;
import com.comp5590.managers.SessionManager;

/**
 * Upon update events on the user object, update the currentUser field on
 * the session manager
 */
public class UpdateSessionManagerListener implements UserListener {

    private final DatabaseManager databaseManager = DatabaseManager.getInstance();
    private final SessionManager sessionManager = SessionManager.getInstance();

    /**
     * Update the current user in the session manager
     * @param event the event to be processed
     * @return the event
     */
    @Override
    public UserUpdateEvent onUserUpdate(UserUpdateEvent event) {
        if (!event.isCancelled()) {
            sessionManager.setCurrentUser(databaseManager.get(User.class, event.getUserId()));
        }
        return event;
    }
}
