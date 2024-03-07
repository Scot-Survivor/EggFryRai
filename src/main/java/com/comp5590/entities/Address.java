package com.comp5590.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

@Entity
@Table(name = "address")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "addressId")
    private int addressId;

    @Column(name = "postCode")
    private String postCode;

    @Column(name = "country")
    private String country;

    @Column(name = "addressLineOne")
    private String addressLineOne;

    @Column(name = "addressLineTwo")
    private String addressLineTwo;

    @Column(name = "addressLineThree")
    private String addressLineThree;

    @OneToMany(mappedBy = "address")
    @ToString.Exclude
    private List<User> users;

    @OneToMany(mappedBy = "roomId")
    @ToString.Exclude
    private List<Room> rooms;

    public Address(String lineOne, String lineTwo, String lineThree, String country, String postCode) {
        this.addressLineOne = lineOne;
        this.addressLineTwo = lineTwo;
        this.country = country;
        this.postCode = postCode;
        this.addressLineThree = lineThree;
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
        Address address = (Address) o;
        return Objects.equals(getAddressId(), address.getAddressId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
            : getClass().hashCode();
    }
}
