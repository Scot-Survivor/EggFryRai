package com.comp5590.tests.basic;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.comp5590.database.entities.*;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.enums.CommunicationPreference;
import com.comp5590.enums.UserRole;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class BookingTests extends SetupTests {

    @Test
    public void testBookingEntityCreation() throws ParseException {
        // create a db manager
        DatabaseManager db = DatabaseManager.getInstance();

        // create user entity
        User patient = makePatient();
        db.save(patient);

        // create doctor entity
        User doctor = makeDoctor();
        db.save(doctor);

        // create a date and time for the appt
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date apptTime = formatDate.parse("2025-01-05 00:00:00");

        // create booking object
        Booking booking = new Booking();
        booking.setDoctor(doctor);
        booking.setPatient(patient);
        booking.setApptTime(apptTime);

        // make and save room
        Room room = makeRoom();
        db.save(room);
        booking.setRoom(room);

        // check saving of booking was successfull
        assertTrue(db.save(booking));
    }

    /**
     * Make a doctor user
     * @return User object
     */
    private User makeDoctor() {
        DatabaseManager db = DatabaseManager.getInstance();

        User doctor = new User();

        // make and save address
        Address address = makeAddress();
        db.save(address);
        doctor.setAddress(address);

        doctor.setRole(UserRole.DOCTOR);
        doctor.setCommunicationPreference(CommunicationPreference.EMAIL);
        doctor.setFirstName("Example");
        doctor.setSurName("Example");
        doctor.setFax("01227872020");
        doctor.setPhone("01227872020");
        doctor.setAdditionalNotes("Test");
        doctor.setAuthenticationDetails(createAuth("rhys2@gmail.com"));
        return doctor;
    }

    /**
     * Make a patient user
     * @return User object
     */
    private User makePatient() {
        DatabaseManager db = DatabaseManager.getInstance();

        User patient = new User();

        // make and save address
        Address address = makeAddress();
        db.save(address);
        patient.setAddress(address);

        patient.setRole(UserRole.PATIENT);
        patient.setCommunicationPreference(CommunicationPreference.EMAIL);
        patient.setFirstName("Example");
        patient.setSurName("Example");
        patient.setFax("01227872020");
        patient.setPhone("01227872020");
        patient.setAdditionalNotes("Test");
        patient.setAuthenticationDetails(createAuth("rhys1@gmail.com"));
        return patient;
    }

    /**
     * Make an address
     * @return Address object
     */
    private Address makeAddress() {
        Address address = new Address("1234 Example St", "Test", "AB", "12345", "");
        return address;
    }

    private AuthenticationDetails createAuth(String email) {
        AuthenticationDetails authDetails = new AuthenticationDetails();
        authDetails.setEmail(email);
        authDetails.setPassword("password");
        authDetails.setTwoFactorEnabled(false);
        return authDetails;
    }

    private Room makeRoom() {
        Room room = new Room();
        Address address = makeAddress();

        DatabaseManager db = DatabaseManager.getInstance();
        db.save(address);

        room.setAddress(address);
        room.setRoomNumber("1");
        return room;
    }
}
