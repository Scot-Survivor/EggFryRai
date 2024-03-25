package com.comp5590.utils;

public class ScreenUtils {

    // given a screenClass, check if @AuthRequired annotation is present
    public static boolean isAuthRequired(Class<?> screenClass) {
        return screenClass.isAnnotationPresent(
            com.comp5590.security.managers.authentication.annotations.AuthRequired.class
        );
    }
}
