package com.comp5590.database.entities;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "room", uniqueConstraints = { @UniqueConstraint(columnNames = "roomNumber") })
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "bookingId")
    @ToString.Exclude
    private List<Booking> bookings;

    // Stored as string for room name "001"
    @Column(name = "roomNumber", length = 25, nullable = false, unique = true)
    private String roomNumber;

    public Room(String roomNumber, Address address) {
        this.roomNumber = roomNumber;
        this.address = address;
    }
}
