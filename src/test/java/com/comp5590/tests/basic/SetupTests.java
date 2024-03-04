package com.comp5590.tests.basic;

import com.comp5590.configuration.AppConfig;
import org.junit.jupiter.api.BeforeAll;

public class SetupTests {
    @BeforeAll
    public static void setup() {
        // Set up the logger
        AppConfig.ConfigFile = "src/test/resources/test.properties";
    }
}
