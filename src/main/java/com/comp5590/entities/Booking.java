package com.comp5590.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "booking")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "bookingId")
    private int bookingId;

    @ManyToOne
    @JoinColumn(name="doctorId")
    private User doctor;

    @ManyToOne
    @JoinColumn(name="patientId", nullable=false)
    private User patient;

    // reference the appt time through a DATETIME object
    @Column (name="apptTime")
    @Temporal(TemporalType.DATE)
    private Date apptTime;

    @ManyToOne
    @JoinColumn (name="roomId", nullable=false)
    private Room room;
}
