package com.notes.notes_app.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notes_user_logs")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userLogId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = true)
    private User changedBy;

    private Long changedOn;

    private boolean isPasswordUpdated = false;
    private boolean isAccountNonLockedStatusUpdated = false;
    private boolean isAccountNonExpiredStatusUpdated = false;
    private boolean isCredentialsNonExpiredStatusUpdated = false;
    private boolean isEnabledStatusUpdated = false;
//    only for 2fa left to store in user change logs
    private boolean isTwoFactorEnabledStatusUpdated = false;
    private boolean isUserDeleted = false;
    private String role;

    private String operation;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

}
