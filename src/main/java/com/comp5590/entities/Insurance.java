package com.comp5590.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

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

    // Join patient table to insurance table
    @OneToOne
    @JoinColumn(name="patientId")
    private Patient patientId;

    @Column(name="insuranceProvider")
    private String insuranceProvider;

    @Temporal(TemporalType.DATE)
    @Column(name="startDate")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name="endDate")
    private Date endDate;
}
