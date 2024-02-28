package com.comp5590.entities;

import com.comp5590.enums.CommunicationPreference;
import jakarta.persistence.*;

@Entity
@Table(name = "doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "doctorId")
    private int doctorId;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "surname")
    private String surname;

    @Column(name="phone")
    private String phone;

    @Column(name="fax")
    private String fax;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "communicationPreference")
    private CommunicationPreference communicationPreference;
}
