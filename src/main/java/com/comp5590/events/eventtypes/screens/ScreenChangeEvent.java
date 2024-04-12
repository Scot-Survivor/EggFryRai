package com.comp5590.events.eventtypes.screens;

import com.comp5590.App;
import com.comp5590.database.entities.User;
import com.comp5590.events.eventtypes.CancellableEvent;
import com.comp5590.managers.LoggerManager;
import com.comp5590.screens.AbstractScreen;
import com.comp5590.security.managers.authentication.annotations.AuthRequired;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.core.Logger;

@Getter
@Setter
public class ScreenChangeEvent extends CancellableEvent {

    private final Logger logger = LoggerManager.getInstance().getLogger(ScreenChangeEvent.class);

    private AbstractScreen screen;
    private boolean hasChanged = false;

    public ScreenChangeEvent(AbstractScreen screen, App app) {
        super("Changing Screen to: " + screen.getClass().getSimpleName(), app, false);
        this.screen = screen;

        // if screen has @AuthRequired annotation, debug log it
        if (screen.getClass().isAnnotationPresent(AuthRequired.class)) {
            // grab user from session manager
            User user = app.getSessionManager().getCurrentUser();
            if (user != null) {
                logger.info(
                    String.format(
                        "Authenticated user %s %s is accessing screen: %s",
                        user.getFirstName(),
                        user.getSurName(),
                        screen.getClass().getSimpleName()
                    )
                );
            } else {
                logger.error(
                    String.format(
                        "Authenticated user is accessing screen: %s, but no user found in session. This should not happen. Contact the developers.",
                        screen.getClass().getSimpleName()
                    )
                );
            }
        } else {
            logger.warn(
                String.format("Unauthenticated user is accessing screen: %s", screen.getClass().getSimpleName())
            );
        }
    }

    public ScreenChangeEvent() {
        super();
        this.screen = null;
    }
}
