package com.notes.notes_app.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "notes_password_reset")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passwordResetId;

    @Column(nullable = false, unique = true)
    private String token;

    private boolean isUsed;

    @Column(nullable = false)
    private Instant expirationTime;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = true)
    private User forUser;

    public PasswordResetToken(String token, Instant expirationTime, User user) {
        this.expirationTime = expirationTime;
        this.token = token;
        this.forUser = user;
    }
}
