package com.comp5590.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

import com.comp5590.validators.annontations.ChronologicalDates;

@Entity
@Table(name = "insurance")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ChronologicalDates
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "patientId")
    private int insuranceId;

    // Join patient table to insurance table
    @OneToOne
    @JoinColumn(name = "patientId")
    private User userId;

    @Column(name = "insuranceProvider")
    private String insuranceProvider;

    @Temporal(TemporalType.DATE)
    @Column(name = "startDate")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "endDate")
    private Date endDate;
}
