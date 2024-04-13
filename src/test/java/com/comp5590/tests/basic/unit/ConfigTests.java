package com.comp5590.tests.basic.unit;

import static org.junit.jupiter.api.Assertions.*;

import com.comp5590.configuration.AppConfig;
import com.comp5590.tests.basic.SetupTests;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Order(1) // Run this test first
@Execution(ExecutionMode.CONCURRENT) // Run tests concurrently
public class ConfigTests extends SetupTests {

    private static AppConfig testConfig;
    private static FileBasedConfiguration trueConfig;

    @BeforeAll
    public static void setup() {
        AppConfig.ConfigFile = "src/test/resources/tested-config.properties"; // Used a different file to test on
        testConfig = AppConfig.getInstance();
        testConfig.reload(); // Force reload
        // load the properties file ourselves to test against
        File configFile = new File(AppConfig.ConfigFile);
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<
            FileBasedConfiguration
        >(PropertiesConfiguration.class)
            .configure(params.fileBased().setFile(configFile));
        try {
            trueConfig = builder.getConfiguration();
        } catch (ConfigurationException e) {
            throw new RuntimeException(e); // No loggers for tests
        }
    }

    // Test that the configuration file is loaded
    @Test
    @Order(1)
    public void testConfigFileLoaded() {
        assertNotNull(testConfig);
    }

    // Test that all the properties in trueConfig are in testConfig
    @Test
    @Order(2)
    public void testAllPropertiesLoaded() {
        List<Field> fields = testConfig.getConfigFields();
        Iterator<String> trueKeys = trueConfig.getKeys();
        Iterator<Field> fieldsIter = fields.iterator();
        while (trueKeys.hasNext() && fieldsIter.hasNext()) {
            String key = trueKeys.next();
            Field field = fieldsIter.next();
            assertNotNull(field);
            assertNotNull(key);
            Object value = testConfig.getValue(key);
            // Next check the values are equal between testConfig or trueConfig
            Object trueValue = trueConfig.getProperty(key);
            assertNotNull(trueValue);
            assertEquals(
                ConvertUtils.convert(trueValue, field.getType()),
                ConvertUtils.convert(value, field.getType())
            );
        }
    }

    @Test
    @Order(3)
    public void testPropertySetViaMethod() {
        testConfig.setValue("LOG_LEVEL", "ERROR", false); // We do not want to save. This is a test
        assertEquals("ERROR", AppConfig.LOG_LEVEL);
        testConfig.setValue("LOG_LEVEL", "DEBUG", false); // There is no way to force order reliably in JUnit, so we have to reset the value
    }

    @Test
    public void testCleanConfigMethod() {
        // Write a temp value to the test.properties file and reload
        File configFile = new File(AppConfig.ConfigFile);
        String testLine = "INVALID_FIELD = 0\n";
        // Append the line to file
        try {
            java.nio.file.Files.write(
                configFile.toPath(),
                testLine.getBytes(),
                java.nio.file.StandardOpenOption.APPEND
            );
        } catch (Exception e) {
            fail("Failed to write to file: " + e.getMessage());
        }

        // Ensure the line was written
        try {
            String fileContents = new String(java.nio.file.Files.readAllBytes(configFile.toPath()));
            assertNotNull(fileContents);
            assertFalse(fileContents.isEmpty());
            assertTrue(fileContents.contains(testLine));
        } catch (Exception e) {
            fail("Failed to read from file: " + e.getMessage());
        }

        // Reload the config
        testConfig.reload();
        List<String> removed = testConfig.cleanConfig();
        assertTrue(removed.contains("INVALID_FIELD"));
    }
}
