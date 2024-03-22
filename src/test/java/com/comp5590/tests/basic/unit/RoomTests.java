package com.comp5590.tests.basic.unit;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.Room;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.tests.basic.SetupTests;
import org.junit.jupiter.api.Test;

public class RoomTests extends SetupTests {

    @Test
    public void testRoomCreation() {
        // create address for the room
        Address address = createAddress();

        // Create the room
        Room room = makeRoom("2", address);

        // test saving the room
        DatabaseManager db = DatabaseManager.getInstance();
        assertTrue(db.save(room));
    }
}
