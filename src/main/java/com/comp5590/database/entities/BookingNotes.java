package com.comp5590.database.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bookingnotes")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookingNotes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bookingId")
    private int bookingnotesId;

    @OneToOne
    @JoinColumn(name = "bookingId")
    private Booking bookingId;
    
    @Column(name = "notes", length = 1024, nullable = false)
    private String notes;

    @Column(name = "prescription", length = 1024, nullable = false)
    private String prescription;
}
