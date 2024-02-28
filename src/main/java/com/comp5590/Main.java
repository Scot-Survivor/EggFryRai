package com.comp5590;

/**
 * This main class is needed to trick Gradle and Java into thinking we're not extending "Application" from JavaFX
 * <a href="https://github.com/javafxports/openjdk-jfx/issues/236#issuecomment-426583174">
 *     This comment outlines the solution
 * </a>
 */
public class Main {

    public static void main(String[] args) {
        HelloFX.main(args);
    }
}