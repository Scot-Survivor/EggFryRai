package com.comp5590.database.entities;

import jakarta.persistence.*;
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

    // name of the prescription
    @Column(name = "prescriptionName", nullable = false)
    private String prescriptionName;

    // recommended dose of the prescription (e.g., 1 pill every 4 hours)
    @Column(name = "recommendedDose", nullable = false)
    private String recommendedDose;

    // many Prescriptions can be assigned to one VisitDetails object
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "visitDetailsId", nullable = false)
    private VisitDetails visitDetails;

    // create a prescription
    public Prescription(String prescriptionName, String recommendedDose, VisitDetails visitDetails) {
        this.prescriptionName = prescriptionName;
        this.recommendedDose = recommendedDose;
        this.visitDetails = visitDetails;
    }
}
