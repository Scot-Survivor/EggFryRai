package com.comp5590.entities;

import com.comp5590.enums.CommunicationPreference;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "doctor")
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

    public int getDoctorId() {
        return doctorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CommunicationPreference getCommunicationPreference() {
        return communicationPreference;
    }

    public void setCommunicationPreference(CommunicationPreference communicationPreference) {
        this.communicationPreference = communicationPreference;
    }
}
