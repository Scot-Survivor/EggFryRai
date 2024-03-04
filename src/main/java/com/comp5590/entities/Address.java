package com.comp5590.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "address")
@Data
public class Address{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "addressId")
    private int addressId;

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
    private List<Patient> patients;

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
