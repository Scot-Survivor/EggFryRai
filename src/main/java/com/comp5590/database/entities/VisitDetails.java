package com.comp5590.database.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "visitdetails")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VisitDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "visitDetailsId")
    private int visitDetailsId;

    // whether a follow up is required or not, for the sake of keeping track of the
    // condition
    @Column(name = "followUpRequired", nullable = false)
    private boolean followUpRequired;

    // notes from the visit, including symptoms
    @Column(name = "notes", nullable = false)
    private String notes;

    // potential diagnosis (not guaranteed to be made)
    @Column(name = "diagnosis", nullable = true)
    private String diagnosis;

    // advice given to the patient during their appointment
    @Column(name = "advice", nullable = false)
    private String advice;

    // the time the details of this visit were added
    @Column(name = "timeAdded", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeAdded;

    // reference the booking through a booking object
    @OneToOne
    @JoinColumn(name = "visitDetailsId")
    private Booking booking;

    // list of prescribed medicines to the patient
    @OneToMany
    @JoinColumn(name = "visitDetailsId", updatable = false, insertable = false)
    @ToString.Exclude
    private List<Prescription> prescriptions;

    // with diagnosis
    public VisitDetails(
        boolean followUpRequired,
        String notes,
        String diagnosis,
        String advice,
        Date timeAdded,
        Booking booking
    ) {
        this.followUpRequired = followUpRequired;
        this.notes = notes;
        this.diagnosis = diagnosis;
        this.advice = advice;
        this.timeAdded = timeAdded;
        this.booking = booking;
    }

    // without diagnosis
    public VisitDetails(boolean followUpRequired, String notes, String advice, Date timeAdded, Booking booking) {
        this.followUpRequired = followUpRequired;
        this.notes = notes;
        this.advice = advice;
        this.timeAdded = timeAdded;
        this.booking = booking;
    }
}
