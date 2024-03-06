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
    @JoinColumn(name="doctorId", nullable=false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name="patientId", nullable=true)
    private Patient patient;

    @Column (name="apptTime")
    @Temporal(TemporalType.DATE)
    private Date apptTime;

    // TODO: Add a new entity to store location
}
