package com.notes.notes_app.services;

import com.notes.notes_app.dtos.UserDTOResponse;
import com.notes.notes_app.models.Role;
import com.notes.notes_app.security.response.MessageResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

    UserDTOResponse getUser(Long userId);

    List<UserDTOResponse> getAllUsers();

    @Transactional
    UserDTOResponse deleteAUser(Long userId);

    UserDTOResponse updateRole(Long userId, String role);

    //Reset Password from Admin controller
    MessageResponse updatePassword(Long userId, String newPassword);

    MessageResponse updateAccountLockStatus(Long userId, boolean lock);

    MessageResponse updateAccountExpiryStatus(Long userId, boolean expire);

    MessageResponse updateCredentialsExpiryStatus(Long userId, boolean expire);

    MessageResponse updateAccountEnabledStatus(Long userId, boolean enabled);

    List<Role> getAllRoles();
}
