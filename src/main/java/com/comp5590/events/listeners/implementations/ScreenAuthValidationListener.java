package com.comp5590.events.listeners.implementations;

import com.comp5590.events.eventtypes.screens.ScreenChangeEvent;
import com.comp5590.events.listeners.interfaces.ScreenListener;
import com.comp5590.managers.SessionManager;
import com.comp5590.screens.LoginScreen;
import com.comp5590.screens.MFAScreen;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.HibernateValidator;

public class ScreenAuthValidationListener implements ScreenListener {

    private Validator validator;

    public ScreenAuthValidationListener() {
        this.validator =
        Validation.byProvider(HibernateValidator.class).configure().buildValidatorFactory().getValidator();
    }

    @Override
    public ScreenChangeEvent onScreenChange(ScreenChangeEvent event) {
        if (event.isCancelled()) {
            return event;
        }
        if (event.getScreen() == null) {
            event.setCancelled(true);
            return event;
        }

        if (
            !SessionManager.getInstance().isAuthenticated() &&
            !(event.getScreen().equals(LoginScreen.class) || event.getScreen().equals(MFAScreen.class))
        ) {
            event.setCancelled(true);
            return event;
        }

        return event;
    }
}
