package com.comp5590.entities;

import com.comp5590.validators.annontations.ChronologicalDates;
import jakarta.persistence.*;
import java.util.Date;
import lombok.*;

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
    @JoinColumn(name = "patientId", nullable = false)
    private User userId;

    @Column(name = "insuranceProvider", nullable = false)
    private String insuranceProvider;

    @Temporal(TemporalType.DATE)
    @Column(name = "startDate", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "endDate", nullable = false)
    private Date endDate;
}
