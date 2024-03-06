package com.comp5590.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "insurance")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Insurance {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "patientId")
    private int insuranceId;

    @Column(name="patientIdd")
    private String patientIdd;

    @Column(name="insuranceProvider")
    private String insuranceProvider;

    @Column(name="startDate")
    private String startDate;

    @Column(name="endDate")
    private String endDate;
}
