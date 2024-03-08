package com.comp5590.tests.basic;

import static org.junit.jupiter.api.Assertions.*;

import com.comp5590.events.eventtypes.CancellableEvent;
import com.comp5590.events.eventtypes.GenericEvent;
import com.comp5590.events.eventtypes.users.LoginEvent;
import com.comp5590.events.eventtypes.users.LogoutEvent;
import com.comp5590.events.listeners.interfaces.Listener;
import com.comp5590.events.listeners.interfaces.UserListener;
import com.comp5590.events.managers.EventManager;
import com.comp5590.managers.LoggerManager;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class EventTests extends SetupTests {

    private final Logger logger = LoggerManager.getInstance().getLogger(EventTests.class, "DEBUG");
    private static final Set<Class<?>> ignoredClasses = new HashSet<>();

    @Getter
    public class TestListener implements UserListener, Listener {

        private boolean loginCalled = false;
        private boolean logoutCalled = false;
        private Set<GenericEvent> calledEvents = new HashSet<>();

        public TestListener() {}

        @Override
        public LoginEvent onLogin(LoginEvent event) {
            eventLogger.debug("Login event called");
            this.loginCalled = true;
            return event;
        }

        @Override
        public LogoutEvent onLogout(LogoutEvent event) {
            eventLogger.debug("Logout event called");
            this.logoutCalled = true;
            return event;
        }

        @Override
        public GenericEvent onGenericEvent(GenericEvent event) {
            calledEvents.add(event);
            return event;
        }
    }

    private Set<Class<? extends GenericEvent>> getEvents() {
        Collections.addAll(ignoredClasses, CancellableEvent.class);
        Reflections reflections = new Reflections("com.comp5590.events.eventtypes", Scanners.SubTypes);
        Set<Class<? extends GenericEvent>> events = new HashSet<>();
        for (Class<? extends GenericEvent> event : reflections.getSubTypesOf(GenericEvent.class)) {
            if (ignoredClasses.contains(event)) {
                continue;
            }
            events.add(event);
        }
        return events;
    }

    @Test
    public void testAllEventsHave0ArgConstructor() {
        Set<Class<? extends GenericEvent>> events = getEvents();
        for (Class<? extends GenericEvent> event : events) {
            try {
                event.getConstructor();
            } catch (NoSuchMethodException e) {
                fail("Event " + event.getName() + " does not have a 0 arg constructor");
            }
        }
    }

    @Test
    public void testListenerGetsInvoked() {
        EventManager eventManager = new EventManager();
        TestListener listener = new TestListener();
        eventManager.addListener(listener);
        eventManager.callEvent(new LoginEvent(null, null, null));
        assertTrue(listener.isLoginCalled());
        eventManager.callEvent(new LogoutEvent(null, null, null));
        assertTrue(listener.isLogoutCalled());
    }

    @Test
    public void testAllEventTypes()
        throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        EventManager eventManager = new EventManager();
        TestListener listener = new TestListener();
        eventManager.addListener(listener);

        // Get the subtypes of GenericEvent
        Set<Class<? extends GenericEvent>> events = getEvents();
        for (Class<? extends GenericEvent> event : events) {
            if (ignoredClasses.contains(event)) {
                continue;
            }
            eventManager.callEvent(event.getConstructor().newInstance());
            events.add(event);
        }
        assertEquals(events.size(), listener.getCalledEvents().size());
        // Assert all events in events are the same as in calledEvents
        for (Class<? extends GenericEvent> event : events) {
            assertTrue(listener.getCalledEvents().stream().anyMatch(e -> e.getClass().equals(event)));
        }
    }
}
