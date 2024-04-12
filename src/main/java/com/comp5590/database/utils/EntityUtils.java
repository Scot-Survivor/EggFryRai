package com.comp5590.database.utils;

import com.comp5590.database.entities.*;
import com.comp5590.database.managers.DatabaseManager;
import com.comp5590.enums.CommunicationPreference;
import com.comp5590.enums.UserRole;
import com.comp5590.security.managers.passwords.PasswordManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntityUtils {

    // QOL Of Methods
    public static DatabaseManager getDbManager() {
        return DatabaseManager.getInstance();
    }

    // * Methods for creating objects in the database
    /**
     * Create an address object with default values.
     *
     * @return Address
     */
    public static Address createAddress() {
        Address address = new Address();
        address.setPostCode("CT1 1AA");
        address.setCountry("GB");
        address.setAddressLineOne("1 Test Street");
        address.setAddressLineTwo("Test Town");
        address.setAddressLineThree("Test County");
        address = getDbManager().saveGet(address);
        return address;
    }

    // Create an address object with the fields specified
    public static Address createAddress(
        String postCode,
        String country,
        String addressLineOne,
        String addressLineTwo,
        String addressLineThree
    ) {
        Address address = new Address();
        address.setPostCode(postCode);
        address.setCountry(country);
        address.setAddressLineOne(addressLineOne);
        address.setAddressLineTwo(addressLineTwo);
        address.setAddressLineThree(addressLineThree);
        address = getDbManager().saveGet(address);
        return address;
    }

    // save address to db with address object
    public static Address createAddress(Address address) {
        address = getDbManager().saveGet(address);
        return address;
    }

    /**
     * Create authentication details without mfa
     *
     * @return AuthenticationDetails
     */
    public static AuthenticationDetails createAuthenticationDetails(String email, String password) {
        AuthenticationDetails authenticationDetails = new AuthenticationDetails();
        authenticationDetails.setEmail(email);
        authenticationDetails.setPassword(PasswordManager.getInstanceOf("Argon2").hashPassword(password));
        authenticationDetails.setTwoFactorEnabled(false);
        authenticationDetails.setAuthenticationToken("");
        authenticationDetails.setRecoveryCodes("");

        // otherwise, just save the authentication details and return it
        return getDbManager().saveGet(authenticationDetails);
    }

    /**
     * Create authentication details with mfa
     *
     * @return AuthenticationDetails
     */
    public static AuthenticationDetails createAuthenticationDetails(
        String email,
        String password,
        String authenticationToken,
        String recoveryCodes
    ) {
        AuthenticationDetails authenticationDetails = createAuthenticationDetails(email, password);
        authenticationDetails.setTwoFactorEnabled(true);
        authenticationDetails.setAuthenticationToken(authenticationToken);
        authenticationDetails.setRecoveryCodes(recoveryCodes);
        // update
        getDbManager().update(authenticationDetails);
        authenticationDetails = getDbManager().get(AuthenticationDetails.class, authenticationDetails.getId());
        return authenticationDetails;
    }

    /**
     * Create a patient user with all filled values,
     *
     * @return User
     */
    public static User createPatient(AuthenticationDetails authenticationDetails) {
        Address address = createAddress();
        User user = new User(
            "Test",
            "User",
            "0123456789",
            "0123456789",
            "Test Notes",
            CommunicationPreference.EMAIL,
            UserRole.PATIENT,
            address
        );
        user.setRole(UserRole.PATIENT);
        user.setAuthenticationDetails(authenticationDetails);
        user = getDbManager().saveGet(user);
        return user;
    }

    /**
     * Create a user object with the email and password specified.
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @return User
     */
    public static User createPatient(String email, String password) {
        return createPatient(createAuthenticationDetails(email, password));
    }

    /**
     * Create a user object with the email and password specified.
     *
     * @param email               the email of the user
     * @param password            the password of the user
     * @param authenticationToken the authentication token
     * @param recoveryCodes       the recovery codes
     * @return User
     */
    public static User createPatient(String email, String password, String authenticationToken, String recoveryCodes) {
        return createPatient(createAuthenticationDetails(email, password, authenticationToken, recoveryCodes));
    }

    /**
     * Create a User (Patient) object with the
     * authenticationDetails and address specified.
     */
    public static User createPatient(AuthenticationDetails authenticationDetails, Address address) {
        User user = new User(
            "Test",
            "User",
            "0123456789",
            "0123456789",
            "Test Notes",
            CommunicationPreference.EMAIL,
            UserRole.PATIENT,
            authenticationDetails,
            address
        );

        // set user details
        user.setRole(UserRole.PATIENT);
        user.setAuthenticationDetails(authenticationDetails);
        user.setAddress(address);

        // save user to db, and return the saved user
        return getDbManager().saveGet(user);
    }

    /**
     * Create a booking.
     *
     * @param patient
     * @param doctor
     * @param room
     * @param date
     *
     * @return Booking
     */

    public static Booking createBooking(User patient, User doctor, Room room, Date date) {
        Booking booking = new Booking();
        booking.setDoctor(doctor);
        booking.setPatient(patient);
        booking.setApptTime(date);
        booking.setRoom(room);
        booking = getDbManager().saveGet(booking);
        return booking;
    }

    /**
     * Create a prescription entity
     *
     * @param prescriptionName Name of medicine (e.g., Paracetamol 500mg)
     * @param recommendedDose  Dose of medicine (e.g., 1 tablet per day)
     * @param visitDetails     VisitDetails object (e.g., the details of the visit
     *                         where the
     *                         prescription was given)
     * @return The Prescription object
     */
    public static Prescription createPrescription(
        String prescriptionName,
        String recommendedDose,
        VisitDetails visitDetails
    ) {
        Prescription prescription = new Prescription(prescriptionName, recommendedDose, visitDetails);
        prescription = getDbManager().saveGet(prescription);
        return prescription;
    }

    /**
     * Create a new VisitDetails object
     *
     * @param followUpRequired Whether a follow up is required
     * @param notes            Notes from the visit
     * @param diagnosis        Diagnosis of the visit
     * @param advice           Advice given to the patient
     * @param timeAdded        Time the details were added
     * @param booking          The booking object
     * @return The VisitDetails object
     */
    public static VisitDetails createVisitDetails(
        boolean followUpRequired,
        String notes,
        String diagnosis,
        String advice,
        Date timeAdded,
        Booking booking
    ) {
        VisitDetails visitDetails = new VisitDetails(followUpRequired, notes, diagnosis, advice, timeAdded, booking);
        visitDetails = getDbManager().saveGet(visitDetails);
        return visitDetails;
    }

    /**
     * Create a doctor user with all filled values,
     *
     * @return User
     */
    public static User createDoctor(AuthenticationDetails authenticationDetails) {
        Address address = createAddress();
        User user = new User(
            "Test",
            "User",
            "0123456789",
            "0123456789",
            "Test Notes",
            CommunicationPreference.EMAIL,
            UserRole.DOCTOR,
            address
        );
        user.setRole(UserRole.DOCTOR);
        user.setAuthenticationDetails(authenticationDetails);
        int id = getDbManager().saveGetId(user);
        user = getDbManager().get(User.class, id);
        return user;
    }

    // Crate a doctor, given all the fields
    public static User createDoctor(
        String firstname,
        String surname,
        String phone,
        String fax,
        String additionalNotes,
        CommunicationPreference communicationPreference,
        Address address,
        AuthenticationDetails authenticationDetails
    ) {
        User user = new User(
            firstname,
            surname,
            phone,
            fax,
            additionalNotes,
            communicationPreference,
            UserRole.DOCTOR,
            address
        );
        user.setRole(UserRole.DOCTOR);
        user.setAuthenticationDetails(authenticationDetails);
        user.setAddress(address);
        user = getDbManager().saveGet(user);
        return user;
    }

    // create a doctor given the User field
    public static User createDoctor(User user) {
        user.setRole(UserRole.DOCTOR);
        user.setAuthenticationDetails(user.getAuthenticationDetails());
        user.setAddress(user.getAddress());
        user = getDbManager().saveGet(user);
        return user;
    }

    /**
     * Create a user object with the email and password specified.
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @return User
     */
    public static User createDoctor(String email, String password) {
        return createDoctor(createAuthenticationDetails(email, password));
    }

    /**
     * Create a Room object using roomNum and address
     *
     * @param roomNum The number of the room
     * @param address The address of the room
     * @return The room
     */
    public static Room createRoom(String roomNum, Address address) {
        Room room = new Room();
        room.setAddress(address);
        room.setRoomNumber(roomNum);
        room = getDbManager().saveGet(room);
        return room;
    }

    // * Methods for removing objects from the database
    /**
     * Remove an object from the database by id
     */
    public static <T> void remove(final Class<T> type, int id) {
        getDbManager().delete(getDbManager().get(type, id));
    }

    /**
     * Complete remove a user object
     */
    public static void remove(User user) {
        remove(AuthenticationDetails.class, user.getAuthenticationDetails().getId());
        remove(User.class, user.getId());
    }

    // * Methods for grabbing objects from the database
    public static AuthenticationDetails getAuthenticationDetails(String email) {
        return getDbManager().getByProperty(AuthenticationDetails.class, "email", email);
    }

    public static Address getAddress(int id) {
        return getDbManager().get(Address.class, id);
    }

    public static User getUser(String email) {
        return getDbManager().getByProperty(User.class, "authenticationDetails.email", email);
    }

    public static Room getRoom(String roomNum) {
        return getDbManager().getByProperty(Room.class, "roomNumber", roomNum);
    }

    // grab all user bookings
    public static List<Booking> getBookingsForUser(User user) {
        // grab db manager
        DatabaseManager db = DatabaseManager.getInstance();

        // return the list of bookings by user ID property (one-to-many relationship)
        return db.getAllByProperty(Booking.class, "patient", user);
    }

    // grab visit details for a booking (1-to-1 relationship)
    public static VisitDetails getVisitDetailsForBooking(Booking booking) {
        // grab db manager
        DatabaseManager db = DatabaseManager.getInstance();

        // grab the visit details for the booking
        return db.getByProperty(VisitDetails.class, "booking", booking);
    }

    // grab all visit details for a user
    public static List<VisitDetails> getAllVisitDetailsForUser(User user) {
        // grab all bookings for the user
        List<Booking> bookings = getBookingsForUser(user);

        // create a list of visit details, instantiate it
        List<VisitDetails> allVisitDetails = new ArrayList<>();

        // for each booking, grab the visit details
        for (Booking booking : bookings) {
            allVisitDetails.add(getVisitDetailsForBooking(booking));
        }

        // return the list of all visit details
        return allVisitDetails;
    }

    // grab all prescriptions for a visit details object
    public static List<Prescription> getAllPrescriptionsForVisitDetails(VisitDetails visitDetails) {
        // grab db manager
        DatabaseManager db = DatabaseManager.getInstance();

        // return the list of prescriptions by visit details ID property (one-to-many
        // relationship)
        return db.getAllByProperty(Prescription.class, "visitDetails", visitDetails);
    }

    // * Methods for checking if objects exist in the database
    public static boolean authenticationDetailsExists(String email) {
        return getDbManager().getByProperty(AuthenticationDetails.class, "email", email) != null;
    }

    public static boolean userExists(String email) {
        return getDbManager().getByProperty(User.class, "authenticationDetails.email", email) != null;
    }

    public static boolean roomExists(String roomNum) {
        return getDbManager().getByProperty(Room.class, "roomNumber", roomNum) != null;
    }

    public static boolean bookingExists(int bookingId) {
        return getDbManager().get(Booking.class, bookingId) != null;
    }

    public static boolean prescriptionExists(String prescriptionName) {
        return getDbManager().getByProperty(Prescription.class, "prescriptionName", prescriptionName) != null;
    }

    public static boolean visitDetailsExists(int visitDetailsId) {
        return getDbManager().get(VisitDetails.class, visitDetailsId) != null;
    }
}
