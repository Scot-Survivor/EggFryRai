package com.comp5590.utils;

import com.comp5590.configuration.AppConfig;
import com.comp5590.database.entities.Address;
import com.comp5590.database.entities.AuthenticationDetails;
import com.comp5590.database.entities.Room;
import com.comp5590.database.entities.User;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.database.utils.EntityUtils;
import com.comp5590.enums.CommunicationPreference;
import com.comp5590.enums.UserRole;
import com.comp5590.managers.LoggerManager;
import com.comp5590.security.managers.passwords.PasswordManager;
import org.apache.logging.log4j.core.Logger;

public class StartupUtils {

    private static Logger logger = LoggerManager.getInstance().getLogger(StartupUtils.class);

    private static DatabaseManager createDatabase() {
        DatabaseManager db = DatabaseManager.getInstance();
        if (db == null) {
            logger.error("Failed to create database");
            return null;
        } else {
            logger.error("Database created successfully");
            return db;
        }
    }

    private static AuthenticationDetails createAuthDetails(String email, String password) {
        return new AuthenticationDetails(
            email,
            PasswordManager.getInstanceOf(AppConfig.HASH_ALGORITHM).hashPassword(password),
            false,
            null,
            null
        );
    }

    private static Address createAddress(String street, String city, String county, String country, String postalCode) {
        return new Address(street, city, county, country, postalCode);
    }

    private static User createUser(AuthenticationDetails auth, Address address) {
        return new User(
            "Example",
            "User",
            "1234567890",
            "0987654321",
            "This is an example user",
            CommunicationPreference.EMAIL,
            UserRole.PATIENT,
            auth,
            address
        );
    }

    // store room IDs
    private static String[] roomIds = new String[] {
        "001",
        "002",
        "003",
        "004",
        "005",
        "006",
        "007",
        "008",
        "009",
        "010",
        "101",
        "102",
        "103",
        "104",
        "105",
        "106",
        "107",
        "108",
        "109",
        "110",
        "201",
        "202",
        "203",
        "204",
        "205",
        "206",
        "207",
        "208",
        "209",
        "210",
        "301",
        "302",
        "303",
        "304",
        "305",
        "306",
        "307",
        "308",
        "309",
        "310",
    };

    // create a bunch of random rooms
    private static Room[] createRooms(Address address) {
        Room[] rooms = new Room[roomIds.length];
        for (int i = 0; i < roomIds.length; i++) {
            rooms[i] = new Room(address, roomIds[i]);
        }
        return rooms;
    }

    // doctors
    private static User[] doctors = new User[] {
        new User(
            "Dr",
            "Health",
            "1234567890",
            "0987654321",
            "This doctor specializes in treating severe AIDS.",
            CommunicationPreference.EMAIL,
            UserRole.DOCTOR,
            createAuthDetails("drhealth@email.com", "hdiuhdoigog"),
            createAddress("1234 Doctor St", "Doctor City", "Doctor County", "Doctor Country", "D1D 1D1")
        ),
        new User(
            "Dr",
            "Bones",
            "2345678901",
            "9876543210",
            "This doctor is an expert in treating broken hearts.",
            CommunicationPreference.PHONE,
            UserRole.DOCTOR,
            createAuthDetails("drbones@email.com", "sdfghjkl"),
            createAddress("5678 Bone St", "Bone City", "Bone County", "Bone Country", "B2B 2B2")
        ),
        new User(
            "Dr",
            "Smiles",
            "3456789012",
            "8765432109",
            "This doctor's laughter is the best medicine.",
            CommunicationPreference.EMAIL,
            UserRole.DOCTOR,
            createAuthDetails("drsmiles@email.com", "qwertyuiop"),
            createAddress("9012 Smile St", "Smile City", "Smile County", "Smile Country", "S3S 3S3")
        ),
        new User(
            "Dr",
            "Pain",
            "4567890123",
            "7654321098",
            "This doctor is a specialist in treating chronic pain.",
            CommunicationPreference.EMAIL,
            UserRole.DOCTOR,
            createAuthDetails("drpain@email.com", "zxcvbnm"),
            createAddress("3456 Pain St", "Pain City", "Pain County", "Pain Country", "P4P 4P4")
        ),
        new User(
            "Dr",
            "Cure",
            "5678901234",
            "6543210987",
            "This doctor is a miracle worker.",
            CommunicationPreference.EMAIL,
            UserRole.DOCTOR,
            createAuthDetails("drcure@email.com", "rfvgyhuj"),
            createAddress("7890 Cure St", "Cure City", "Cure County", "Cure Country", "C5C 5C5")
        ),
    };

    public static User createObjects() {
        // * SINGLE USER CREATION
        // set example email and password for new user
        String email = "a@a.com";
        String password = "a";

        // query db, if user exists, return the existing user
        if (EntityUtils.userExists(email)) {
            logger.info("User already exists in database. Good.");
            return EntityUtils.getUser(email);
        }
        // otherwise, create a new user
        else {
            // create authentication details
            logger.info("Creating authentication details");
            AuthenticationDetails auth;
            if (EntityUtils.authenticationDetailsExists(email)) {
                auth = EntityUtils.getAuthenticationDetails(email);
            } else {
                auth = EntityUtils.createAuthenticationDetails(email, password);
            }
            logger.info("Authentication details created successfully");
            // create an address
            logger.info("Creating address");
            Address address = EntityUtils.createAddress();
            logger.info("Address created successfully");

            // save the user to the database
            User user = EntityUtils.createPatient(auth, address);

            // if null, return null
            if (user == null) {
                logger.error("Failed to create test user at startup.");
                return null;
            }
        }

        // * DOCTORS CREATION
        User[] doctors = StartupUtils.doctors;

        // for each doctor, if doctor (user) does not already exist in DB, make him
        logger.info("Creating doctors");
        for (User doctor : doctors) {
            if (!EntityUtils.userExists(doctor.getAuthenticationDetails().getEmail())) {
                // create doctor auth
                AuthenticationDetails auth = EntityUtils.createAuthenticationDetails(
                    doctor.getAuthenticationDetails().getEmail(),
                    "password"
                );
                // create doctor address
                Address address = EntityUtils.createAddress(doctor.getAddress());

                // set doctor's auth & address
                doctor.setAuthenticationDetails(auth);
                doctor.setAddress(address);

                User doc = EntityUtils.createDoctor(doctor);
                if (doc == null) {
                    logger.error(
                        "Failed to create doctor + " +
                        doctor.getFirstName() +
                        " " +
                        doctor.getSurName() +
                        " at startup."
                    );
                    return null;
                }
            }
        }
        logger.info("Doctors created successfully");

        // * ROOM CREATION (with default address of GP clinic)
        // create new default address for GP clinic, save to DB
        Address gpClinicAddress = new Address(
            "1234 GP Clinic St",
            "GP Clinic City",
            "GP Clinic County",
            "GP Clinic Country",
            "A1A 1A1"
        );

        // create a bunch of rooms with the GP's address & save them to the database
        Room[] rooms = createRooms(gpClinicAddress);
        for (Room room : rooms) {
            // if room is not in db, save it
            if (EntityUtils.roomExists(room.getRoomNumber())) {
                EntityUtils.makeRoom(room.getRoomNumber(), gpClinicAddress);
            }
        }

        // if everything is successful & new user exists in db, return user object
        return EntityUtils.getUser(email);
    }
}
