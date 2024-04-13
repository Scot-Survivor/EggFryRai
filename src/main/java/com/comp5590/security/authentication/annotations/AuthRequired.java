package com.comp5590.security.authentication.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation that indicate whether a screen should be checked for authentication
 */
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Documented
public @interface AuthRequired {
}
