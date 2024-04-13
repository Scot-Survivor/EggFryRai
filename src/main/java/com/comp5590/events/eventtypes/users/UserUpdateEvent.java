package com.comp5590.events.eventtypes.users;

import com.comp5590.events.enums.UserAttribute;
import com.comp5590.events.eventtypes.CancellableEvent;
import lombok.Getter;

@Getter
public class UserUpdateEvent extends CancellableEvent {

    private final UserAttribute attribute;
    private final String value;
    private final int userId;

    public UserUpdateEvent(UserAttribute attribute, String value, int userId) {
        this.attribute = attribute;
        this.value = value;
        this.userId = userId;
    }

    public UserUpdateEvent() {
        this.attribute = null;
        this.value = null;
        this.userId = -1;
    }
}
