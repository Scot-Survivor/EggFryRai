package com.comp5590.entities;

import com.comp5590.enums.CommunicationPreference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "doctor")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Doctor implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "doctorId")
    private int doctorId;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "surname")
    private String surname;

    @Column(name="phone")
    private String phone;

    @Column(name="fax")
    private String fax;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "communicationPreference")
    private CommunicationPreference communicationPreference;

    @OneToMany(mappedBy="bookingId")
    @ToString.Exclude
    private List<Booking> bookings;

    public Doctor(String firstName, String surname, String phone, String fax, String mail,
                  CommunicationPreference communicationPreference) {
        this.firstName = firstName;
        this.surname = surname;
        this.phone = phone;
        this.fax = fax;
        this.email = mail;
        this.communicationPreference = communicationPreference;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(getDoctorId(), doctor.getDoctorId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
