package com.comp5590.entities;

import jakarta.persistence.*;

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

    @Column(name="addressLineThree")
    private String addressLineTwo;

    @Column(name="addressLineThree")
    private String addressLineThree;
}
