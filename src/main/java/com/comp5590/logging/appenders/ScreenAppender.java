package com.comp5590.logging.appenders;

import com.comp5590.App;
import com.comp5590.screens.misc.LoggingScreen;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "ScreenAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
public class ScreenAppender extends AbstractAppender {

    private LoggingScreen loggingScreen;

    public ScreenAppender(String name, Filter filter) {
        super(name, filter, null);
        this.loggingScreen = getLoggingScreen();
    }

    public ScreenAppender(String name, Filter filter, final Layout<? extends Serializable> layout) {
        super(name, filter, layout);
        this.loggingScreen = getLoggingScreen();
    }

    private LoggingScreen getLoggingScreen() {
        if (App.getInstance().getScreenManager() == null) {
            return null;
        }
        return (LoggingScreen) App.getInstance().getScreenManager().getScreenInstance(LoggingScreen.class);
    }

    /**
     * Create the appender
     * @param name the name of the appender
     * @param filter the filter to apply
     * @return the appender
     */
    @PluginFactory
    public static ScreenAppender createAppender(
        @PluginAttribute("name") String name,
        @PluginElement("Filter") Filter filter
    ) {
        return new ScreenAppender(name, filter);
    }

    /**
     * Create the appender with layout
     * @param layout the layout to apply
     */
    @PluginFactory
    public static ScreenAppender createDefaultWithLayout(
        @PluginElement("Layout") Layout<? extends Serializable> layout
    ) {
        return new ScreenAppender("ScreenAppender", null, layout);
    }

    /**
     * Append the log event to the screen
     * @param event the log event
     */
    @Override
    public void append(LogEvent event) {
        if (loggingScreen == null) {
            loggingScreen = getLoggingScreen();
            if (loggingScreen == null) {
                return;
            } else {
                this.append(event);
            }
        }
        // String formatted of current time
        String formattedTime = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
        loggingScreen.addLog(
            "[" + formattedTime + " | " + event.getLevel() + "] " + event.getMessage().getFormattedMessage()
        );
    }
}
