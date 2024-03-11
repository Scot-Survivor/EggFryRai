package com.comp5590.tests.basic;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.comp5590.database.managers.DatabaseManager;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@Order(2)
public class DatabaseTests extends SetupTests {

    @Test
    public void testDatabaseConnection() {
        // Test database connection
        DatabaseManager db = DatabaseManager.getInstance();
        assertTrue(db.testConnection());
    }
}
