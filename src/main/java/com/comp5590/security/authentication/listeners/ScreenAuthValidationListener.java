package com.comp5590.security.authentication.listeners;

import com.comp5590.App;
import com.comp5590.events.eventtypes.screens.ScreenChangeEvent;
import com.comp5590.events.listeners.interfaces.ScreenListener;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.authentication.LoginScreen;
import com.comp5590.screens.managers.ScreenManager;
import com.comp5590.screens.misc.AbstractScreen;
import com.comp5590.security.authentication.annotations.AuthRequired;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

/**
 * Listener that checks if a screen requires authentication and if the user is authenticated.
 * @author Joshua Shiells
 */
public class ScreenAuthValidationListener implements ScreenListener {

    /**
     * Set of screen names that require authentication.
     */
    @Getter
    private final Set<String> protectedScreens;

    private final ScreenManager screenManager;

    public ScreenAuthValidationListener() {
        this.screenManager = App.getInstance().getScreenManager();
        this.protectedScreens = new HashSet<>();
        // Use reflection to find all screens annotated with AuthRequired
        Reflections reflections = new Reflections("com.comp5590.screens", Scanners.SubTypes);
        for (Class<? extends AbstractScreen> screen : reflections.getSubTypesOf(AbstractScreen.class)) {
            if (screen.isAnnotationPresent(AuthRequired.class)) {
                protectedScreens.add(screen.getSimpleName());
            }
        }
    }

    /**
     * Check if the screen requires authentication and if the user is authenticated.
     * @param event ScreenChangeEvent
     * @return ScreenChangeEvent
     */
    @Override
    public ScreenChangeEvent onScreenChange(ScreenChangeEvent event) {
        if (event.isCancelled()) {
            return event;
        }

        if (protectedScreens.contains(event.getScreen().getClass().getSimpleName())) {
            if (!SessionManager.getInstance().isAuthenticated()) {
                event.setCancelled(true);
                screenManager.showScene(LoginScreen.class);
            }
        }

        return event;
    }
}
