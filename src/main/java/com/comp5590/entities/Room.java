package com.comp5590.entities;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "room")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "roomId")
    private int roomId;

    @ManyToOne
    @JoinColumn(name = "address", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "bookingId")
    @ToString.Exclude
    private List<Booking> bookings;

    // Stored as string for room name "001"
    @Column(name = "roomNumber")
    private String roomNumber;
}
