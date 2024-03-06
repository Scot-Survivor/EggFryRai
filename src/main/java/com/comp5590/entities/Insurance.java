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

    @OneToOne
    @JoinColumn(name="patientId")
    private Patient patientId;

    @Column(name="insuranceProvider")
    private String insuranceProvider;

    @Column(name="startDate")
    private Date startDate;

    @Column(name="endDate")
    private Date endDate;
}
