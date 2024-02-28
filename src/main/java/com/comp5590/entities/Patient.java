package com.comp5590.entities;

import com.comp5590.enums.CommunicationPreference;
import jakarta.persistence.*;

@Entity
@Table(name = "patient")
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
}
