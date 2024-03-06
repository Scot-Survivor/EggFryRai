package com.comp5590.entities;

import com.comp5590.enums.CommunicationPreference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "patient")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "patientId")
    private int id;

    // Personal details
    @Column(name="firstName")
    private String firstName;
    @Column(name="surName")
    private String surName;

    @Column(name="phone")
    private String phone;

    @Column(name="fax")
    private String fax;

    @Column(name="additionalNotes")
    private String additionalNotes;

    @Enumerated(EnumType.STRING)
    @Column(name = "communicationPreference")
    private CommunicationPreference communicationPreference;

    // Authentication Details
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;  // This will be base64 encoded hash

    // Address
    @ManyToOne
    @JoinColumn(name="addressId", nullable=false)
    private Address address;

    @OneToMany(mappedBy="bookingId")
    @ToString.Exclude
    private List<Booking> bookings;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Patient patient = (Patient) o;
        return Objects.equals(getId(), patient.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
