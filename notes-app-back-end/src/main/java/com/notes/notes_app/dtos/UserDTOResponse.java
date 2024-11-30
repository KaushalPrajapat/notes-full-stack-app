package com.notes.notes_app.dtos;

import com.notes.notes_app.models.Role;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOResponse {
    private Long userId;
    private String username;
    private String email;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean enabled;

    private LocalDate credentialsExpiryDate;
    private LocalDate accountExpiryDate;

    private boolean isTwoFactorEnabled = false;
    private String signUpMethod;

    private Role role;

    private String createdDate;
    private String updatedDate;

    private String message;

}
