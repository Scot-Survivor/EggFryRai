package com.comp5590.entities;

import com.comp5590.enums.UserRole;
import com.comp5590.validators.annontations.InFuture;
import com.comp5590.validators.annontations.RequiredRole;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bookingId")
    private int bookingId;

    @ManyToOne
    @JoinColumn(name = "doctorId")
    @RequiredRole(UserRole.DOCTOR)
    private User doctor;

    @ManyToOne
    @JoinColumn(name = "patientId", nullable = false)
    @RequiredRole(UserRole.PATIENT)
    private User patient;

    // reference the appt time through a DATETIME object
    @Column(name = "apptTime")
    @Temporal(TemporalType.DATE)
    @InFuture
    private Date apptTime;

    @ManyToOne
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;
}
