package com.comp5590.utils;

import com.comp5590.security.authentication.annotations.AuthRequired;

public class ScreenUtils {

    // given a screenClass, check if @AuthRequired annotation is present
    public static boolean isAuthRequired(Class<?> screenClass) {
        return screenClass.isAnnotationPresent(AuthRequired.class);
    }
}
