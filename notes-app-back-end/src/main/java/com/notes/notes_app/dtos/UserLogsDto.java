package com.notes.notes_app.dtos;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLogsDto {
    private Long userLogId;

    private String operation;
    private String changedBy;
    private Long changedOn;

    private boolean isPasswordUpdated;
    private boolean isAccountNonLockedStatusUpdated;
    private boolean isAccountNonExpiredStatusUpdated;
    private boolean isCredentialsNonExpiredStatusUpdated;
    private boolean isEnabledStatusUpdated;

    private boolean isTwoFactorEnabledStatusUpdated;
    private boolean isUserDeleted;
    private String role;

    private String updatedDate;
}
