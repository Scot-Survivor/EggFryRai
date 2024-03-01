import com.comp5590.configuration.AppConfig;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Order(1) // Run this test first
public class ConfigTests {
    private static AppConfig testConfig;
    private static FileBasedConfiguration trueConfig;
    @BeforeAll
    public static void setup() {
        testConfig = AppConfig.getInstance();
        testConfig.reload(); // Force reload
        // load the properties file ourselves to test against
        File configFile = new File(AppConfig.ConfigFile);
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                .configure(params.fileBased().setFile(configFile));
        try {
            trueConfig = builder.getConfiguration();
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);  // No loggers for tests
        }
    }

    // Test that the configuration file is loaded
    @Test
    public void testConfigFileLoaded() {
        assertNotNull(testConfig);
    }

    // Test that all the properties in trueConfig are in testConfig
    @Test
    public void testAllPropertiesLoaded() {
        List<Field> fields = testConfig.getConfigFields();
        Iterator<String> trueKeys = trueConfig.getKeys();
        Iterator<Field> fieldsIter = fields.iterator();
        while (trueKeys.hasNext()) {
            String key = trueKeys.next();
            Field field = fieldsIter.next();
            assertNotNull(field);
            assertNotNull(key);
            Object value;
            try {
                value = field.get(testConfig);
                assertNotNull(value);  // If this fails, the field is not in the config
            } catch (IllegalAccessException e) {
                fail(e.getMessage());  // Fail if we can't access the field, since we should have filtered out private fields
                return;
            }
            // Next check the values are equal between testConfig or trueConfig
            Object trueValue = trueConfig.getProperty(key);
            assertNotNull(trueValue);
            assertEquals(ConvertUtils.convert(trueValue, field.getType()),
                    ConvertUtils.convert(value, field.getType()));
        }
    }
}
