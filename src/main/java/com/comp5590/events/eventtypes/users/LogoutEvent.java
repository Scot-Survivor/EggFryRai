package com.comp5590.events.eventtypes.users;

import com.comp5590.App;
import com.comp5590.entities.User;

/**
 * Event for when a user logs out, the same as a login event but with a different message
 */

public class LogoutEvent extends LoginEvent {

    public LogoutEvent(String message, App app, User user) {
        super(message, app, user);
    }

    public LogoutEvent(App app, User user) {
        super(formatMessage(user), app, user);
    }

    public LogoutEvent() {
        super();
    }

    private static String formatMessage(User user) {
        return "User " + user.getFirstName() + " " + user.getSurName() + " has logged out";
    }
}
