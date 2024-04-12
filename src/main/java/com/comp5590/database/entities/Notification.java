package com.comp5590.database.entities;

import jakarta.persistence.*;
import java.util.Date;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "notification")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "notificationId")
    private int notificationId;

    // message to be displayed to the user
    @Column(name = "message", nullable = false, length = 1024)
    private String message;

    // current timestamp column upon creation of the notification
    @Column(name = "timestamp", nullable = false)
    @CreationTimestamp
    private Date timestamp;

    @Column(name = "read", nullable = false)
    private boolean read;

    // many notifications can be assigned to one user
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    // create a new notification
    public Notification(String message, boolean read, User user) {
        this.message = message;
        this.read = read;
        this.user = user;
    }

    // create a new notification
    public Notification(String message, User user) {
        this.message = message;
        this.user = user;
        this.read = false;
    }
}
