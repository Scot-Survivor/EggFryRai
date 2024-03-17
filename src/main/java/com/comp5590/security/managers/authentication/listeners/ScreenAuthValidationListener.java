package com.comp5590.security.managers.authentication.listeners;

import com.comp5590.App;
import com.comp5590.events.eventtypes.screens.ScreenChangeEvent;
import com.comp5590.events.listeners.interfaces.ScreenListener;
import com.comp5590.managers.ScreenManager;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.AbstractScreen;
import com.comp5590.screens.LoginScreen;
import com.comp5590.security.managers.authentication.annotations.AuthRequired;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class ScreenAuthValidationListener implements ScreenListener {

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
