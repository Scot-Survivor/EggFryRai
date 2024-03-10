package com.comp5590.entities;

import com.comp5590.validators.annontations.ValidMFA;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "authenticationDetails", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ValidMFA // Force 2FA restrictions
public class AuthenticationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "authenticationId")
    private int id;

    // Authentication Details
    @NotNull
    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @NotNull
    @Column(name = "password", nullable = false, length = 1024)
    @ToString.Exclude
    private String password; // This will be base64 encoded hash

    @NotNull
    @Column(name = "twoFactorEnabled")
    private boolean twoFactorEnabled;

    @Column(name = "authenticationToken", nullable = true)
    private String authenticationToken;

    /**
     * The recovery codes are used to recover the account if the user loses access to their 2FA device
     * They're split by a comma
     */
    @Column(name = "recoveryCodes", nullable = true)
    @ToString.Exclude
    private String recoveryCodes;

    @OneToOne(mappedBy = "authenticationDetails")
    @ToString.Exclude
    private User user;

    public AuthenticationDetails(
        String email,
        String password,
        boolean twoFactorEnabled,
        String authenticationToken,
        String recoveryCodes
    ) {
        this.email = email;
        this.password = password;
        this.twoFactorEnabled = twoFactorEnabled;
        this.authenticationToken = authenticationToken;
        this.recoveryCodes = recoveryCodes;
    }
}
