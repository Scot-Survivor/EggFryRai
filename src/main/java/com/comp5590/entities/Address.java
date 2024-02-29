package com.comp5590.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "address")
public class Address{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "addressId")
    private int addressId;
    
    // whole lotta postcodes
    @Column(name="postCode")
    private String postCode;

    @Column(name="country")
    private String country;

    @Column(name="addressLineOne")
    private String addressLineOne;

    @Column(name="addressLineTwo")
    private String addressLineTwo;

    @Column(name="addressLineThree")
    private String addressLineThree;

    @OneToMany(mappedBy="address")
    private List<Patient> patient;

    public Address() {
    }

    public Address(String postCode, String country, String addressLineOne, String addressLineTwo, String addressLineThree) {
        this.postCode = postCode;
        this.country = country;
        this.addressLineOne = addressLineOne;
        this.addressLineTwo = addressLineTwo;
        this.addressLineThree = addressLineThree;
    }
}
