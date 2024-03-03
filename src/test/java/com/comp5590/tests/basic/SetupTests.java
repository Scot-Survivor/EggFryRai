package com.comp5590.tests.basic;

import com.comp5590.configuration.AppConfig;
import org.junit.jupiter.api.BeforeAll;

public class SetupTests {
    @BeforeAll
    public static void setup() {
        // Set up the logger
        AppConfig.ConfigFile = "src/test/resources/test.properties";
        System.setProperty("prism.verbose", "true"); // optional
        System.setProperty("java.awt.headless", "true");
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("testfx.setup.timeout", "2500");
    }
}
