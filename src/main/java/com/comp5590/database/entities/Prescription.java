package com.comp5590.database.entities;

import com.comp5590.database.validators.annontations.RequiredRole;
import com.comp5590.enums.UserRole;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "prescription")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "prescriptionId")
    private int prescriptionId;

    // Additional notes for the prescription
    @Column(name = "additionalNotes", nullable = false)
    private String additionalNotes;

    // We have a date its been prescribed on so we know how long its valid for
    @Column(name = "dateOfPrescription", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateOfPrescription;

    // Links to user for things like dob should we want to print off prescription
    @OneToOne
    @JoinColumn(name = "patientId", nullable = false)
    @RequiredRole(UserRole.PATIENT)
    private User patient;

    // Links to doctor to prove who wrote the prescription
    @OneToOne
    @JoinColumn(name = "doctorId", nullable = false)
    @RequiredRole(UserRole.DOCTOR)
    private User doctor;

    // List of medicines that the user uses
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "medicineId")
    private List<Medicine> medicine;

    @PrePersist
    protected void onCreate() { // Set the creation time to the creation of the entity
        dateOfPrescription = LocalDateTime.now();
    }
}
