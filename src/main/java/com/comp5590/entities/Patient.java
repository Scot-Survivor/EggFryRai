package com.comp5590.entities;

import com.comp5590.enums.CommunicationPreference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "patient")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "patientId")
    private int id;

    // Personal details
    @Column(name="firstName")
    private String firstName;
    @Column(name="surName")
    private String surName;

    @Column(name="phone")
    private String phone;

    @Column(name="fax")
    private String fax;

    @Column(name="additionalNotes")
    private String additionalNotes;

    @Enumerated(EnumType.STRING)
    @Column(name = "communicationPreference")
    private CommunicationPreference communicationPreference;

    // Authentication Details
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;  // This will be base64 encoded hash

    @Column(name = "twoFactorEnabled")
    private boolean twoFactorEnabled;

    @Column(name = "authenticationToken")
    private String authenticationToken;
    /**
     * The recovery codes are used to recover the account if the user loses access to their 2FA device
     * They're split by a comma
     */
    @Column(name = "recoveryCodes")
    @ToString.Exclude
    private String recoveryCodes;

    // Address
    @ManyToOne
    @JoinColumn(name="addressId", nullable=false)
    private Address address;

    @OneToMany(mappedBy="bookingId")
    @ToString.Exclude
    private List<Booking> bookings;

    /**
     * Constructor for all required fields
     */
    public Patient(String firstName, String surName, String phone, String fax, String additionalNotes,
                   CommunicationPreference communicationPreference, String email, String password,
                   boolean twoFactorEnabled, String authenticationToken, String recoveryCodes, Address address) {
        this.firstName = firstName;
        this.surName = surName;
        this.phone = phone;
        this.fax = fax;
        this.additionalNotes = additionalNotes;
        this.communicationPreference = communicationPreference;
        this.email = email;
        this.password = password;
        this.twoFactorEnabled = twoFactorEnabled;
        this.authenticationToken = authenticationToken;
        this.recoveryCodes = recoveryCodes;
        this.address = address;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Patient patient = (Patient) o;
        return Objects.equals(getId(), patient.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
