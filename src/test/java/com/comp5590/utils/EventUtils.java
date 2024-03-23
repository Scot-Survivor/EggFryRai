package com.comp5590.utils;

import javafx.event.EventType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;

public class EventUtils {

    // return a mouse event
    public static MouseEvent getMouseEvent() {
        return new javafx.scene.input.MouseEvent(
            javafx.scene.input.MouseEvent.MOUSE_CLICKED,
            0,
            0,
            0,
            0,
            javafx.scene.input.MouseButton.PRIMARY,
            1,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            null
        );
    }

    // create a new method to generate a custom mouse event
    public static MouseEvent createCustomMouseEvent(
        EventType<? extends MouseEvent> eventType,
        double screenX,
        double screenY,
        double sceneX,
        double sceneY,
        MouseButton button,
        int clickCount,
        boolean shiftDown,
        boolean controlDown,
        boolean altDown,
        boolean metaDown,
        boolean primaryButtonDown,
        boolean middleButtonDown,
        boolean secondaryButtonDown,
        boolean synthesized,
        boolean popupTrigger,
        boolean stillSincePress,
        PickResult pickResult
    ) {
        return new javafx.scene.input.MouseEvent(
            eventType,
            screenX,
            screenY,
            sceneX,
            sceneY,
            button,
            clickCount,
            shiftDown,
            controlDown,
            altDown,
            metaDown,
            primaryButtonDown,
            middleButtonDown,
            secondaryButtonDown,
            synthesized,
            popupTrigger,
            stillSincePress,
            pickResult
        );
    }
}
