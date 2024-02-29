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

    // Address
    @ManyToOne
    @JoinColumn(name="addressId", nullable=false)
    private Address address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
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

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public CommunicationPreference getCommunicationPreference() {
        return communicationPreference;
    }

    public void setCommunicationPreference(CommunicationPreference communicationPreference) {
        this.communicationPreference = communicationPreference;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
