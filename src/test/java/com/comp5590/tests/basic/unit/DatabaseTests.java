package com.comp5590.tests.basic.unit;

import static org.junit.jupiter.api.Assertions.*;

import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.enums.UserRole;
import com.comp5590.tests.basic.SetupTests;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@Order(2)
public class DatabaseTests extends SetupTests {

    List<User> users;

    @Test
    public void testDatabaseConnection() {
        // Test database connection
        DatabaseManager db = DatabaseManager.getInstance();
        assertTrue(db.testConnection());
    }

    @BeforeEach
    public void dataSetup() {
        addData();
    }

    private void addData() {
        users = new ArrayList<>();
        users.add(createPatient("example@example.com", "password"));
        for (int i = 0; i < 10; i++) {
            users.add(createDoctor("doctor-" + i + "@example.com", "password"));
            users.add(createPatient("patient-" + i + "@example.com", "password"));
        }
    }

    @Test
    public void testGetAllMethod() {
        List<User> allUsers = DatabaseManager.getInstance().getAll(User.class);
        assertEquals(allUsers.size(), users.size());
    }

    @Test
    public void testGetMethod() {
        User user = users.get(0);
        User dbUser = DatabaseManager.getInstance().get(User.class, user.getId());
        assertEquals(user, dbUser);
    }

    @Test
    public void testGetByPropertyMethod() {
        User user = users.get(0);
        User dbUser = DatabaseManager
            .getInstance()
            .getByProperty(User.class, "authenticationDetails.email", user.getAuthenticationDetails().getEmail());
        assertEquals(user, dbUser);
    }

    @Test
    public void testGetAllByPropertyMethod() {
        User user = users.get(0);
        List<User> dbUsers = DatabaseManager
            .getInstance()
            .getAllByProperty(User.class, "authenticationDetails.email", user.getAuthenticationDetails().getEmail());
        assertEquals(1, dbUsers.size());
        assertEquals(user, dbUsers.get(0));

        List<User> doctors = DatabaseManager.getInstance().getAllByProperty(User.class, "role", UserRole.DOCTOR);
        assertEquals(10, doctors.size());
    }

    @Test
    public void testUpdateMethod() {
        User user = users.get(0);
        user.getAuthenticationDetails().setEmail("UPDATED-EMAIL@example.com");
        DatabaseManager.getInstance().update(user);
        User dbUser = DatabaseManager.getInstance().get(User.class, user.getId());
        assertEquals(user, dbUser);
    }

    @Test
    public void testDeleteMethod() {
        User user = users.get(0);
        DatabaseManager.getInstance().delete(user);
        User dbUser = DatabaseManager.getInstance().get(User.class, user.getId());
        assertNull(dbUser);
    }

    @Test
    public void testTypedQueryMethod() {
        List<User> doctors = DatabaseManager.getInstance().query(User.class, "FROM User WHERE role = 'DOCTOR'");
        assertEquals(10, doctors.size());
    }

    @Test
    public void testQueryMethod() {
        List<?> doctors = DatabaseManager.getInstance().query("FROM User WHERE role = 'DOCTOR'");
        assertEquals(10, doctors.size());
    }
}
