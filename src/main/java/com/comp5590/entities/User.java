package com.comp5590.entities;

import com.comp5590.enums.CommunicationPreference;
import com.comp5590.enums.UserRole;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

@Entity
@Table(name = "patient")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userId")
    private int id;

    // Personal details
    @Column(name = "firstName")
    private String firstName;

    @Column(name = "surName")
    private String surName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "fax")
    private String fax;

    @Column(name = "additionalNotes")
    private String additionalNotes;

    @Enumerated(EnumType.STRING)
    @Column(name = "communicationPreference")
    private CommunicationPreference communicationPreference;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "authenticationId", nullable = false)
    private AuthenticationDetails authenticationDetails;

    // Address
    @ManyToOne
    @JoinColumn(name = "addressId", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "bookingId")
    @ToString.Exclude
    private List<Booking> bookings;

    /**
     * Constructor for all required fields
     */
    public User(
        String firstName,
        String surName,
        String phone,
        String fax,
        String additionalNotes,
        CommunicationPreference communicationPreference,
        Address address
    ) {
        this.firstName = firstName;
        this.surName = surName;
        this.phone = phone;
        this.fax = fax;
        this.additionalNotes = additionalNotes;
        this.communicationPreference = communicationPreference;
        this.address = address;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
            : getClass().hashCode();
    }
}
