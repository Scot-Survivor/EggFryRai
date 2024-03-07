package com.comp5590.entities;

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

    @Column(name = "notes")
    private String notes;

    @Column(name = "prescription")
    private String prescription;
}
