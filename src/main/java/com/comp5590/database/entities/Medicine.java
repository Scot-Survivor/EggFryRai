package com.comp5590.database.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medicine")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "medicineId")
    private int medicineId;

    @Column(name = "medicineName", unique = true)
    private String medicineName;

    @Column(name = "recomendedDose")
    private int recomendedDose;

    @ManyToOne
    @JoinColumn(name = "prescription")
    private Prescription prescription;

    public Medicine(String medicineName, int dose, Prescription prescription) {
        this.medicineName = medicineName;
        this.recomendedDose = dose;
        this.prescription = prescription;
    }
}
