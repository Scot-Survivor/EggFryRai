package com.comp5590.tests.basic;

import com.comp5590.managers.DatabaseManager;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


@Order(2)
public class DatabaseTests extends SetupTests {

    @Test
    public void testDatabaseConnection() {
        // Test database connection
        DatabaseManager db = DatabaseManager.getInstance();
        assertTrue(db.testConnection());
    }
}
