package com.comp5590.events.eventtypes.users;

import com.comp5590.App;
import com.comp5590.entities.User;
import com.comp5590.events.eventtypes.CancellableEvent;
import lombok.Getter;

@Getter
public class LoginEvent extends CancellableEvent {

    private final User user;

    public LoginEvent(App app, User user) {
        super(formatMessage(user), app, false);
        this.user = user;
    }

    public LoginEvent(String message, App app, User user) {
        super(message, app, false);
        this.user = user;
    }

    public LoginEvent() {
        super();
        this.user = null;
    }

    public String getFullName() {
        return user.getFirstName() + " " + user.getSurName();
    }

    public String getEmail() {
        return user.getAuthenticationDetails().getEmail();
    }

    private static String formatMessage(User user) {
        return "User " + user.getFirstName() + " " + user.getSurName() + " has logged in";
    }
}
