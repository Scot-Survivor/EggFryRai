package com.comp5590.entities;

import com.comp5590.enums.CommunicationPreference;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "doctor")
@Data
public class Doctor implements Serializable {
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

    public Doctor(String firstName, String surname, String phone, String fax, String email, CommunicationPreference communicationPreference) {
        this.firstName = firstName;
        this.surname = surname;
        this.phone = phone;
        this.fax = fax;
        this.email = email;
        this.communicationPreference = communicationPreference;
    }

    public Doctor() {

    }
}
