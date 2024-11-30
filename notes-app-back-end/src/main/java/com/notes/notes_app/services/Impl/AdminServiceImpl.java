package com.notes.notes_app.services.Impl;

import com.notes.notes_app.dtos.UserDTOResponse;
import com.notes.notes_app.exceptions.CustomException;
import com.notes.notes_app.models.AppRole;
import com.notes.notes_app.models.Role;
import com.notes.notes_app.models.User;
import com.notes.notes_app.repos.PasswordResetTokenRepository;
import com.notes.notes_app.repos.RoleRepository;
import com.notes.notes_app.repos.UserRepository;
import com.notes.notes_app.security.response.MessageResponse;
import com.notes.notes_app.services.AdminService;
import com.notes.notes_app.services.UserLogsService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserLogsService userLogsService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public UserDTOResponse getUser(Long userId) {
        User user = findUserById(userId);
        UserDTOResponse response = new UserDTOResponse();
        BeanUtils.copyProperties(user, response);
        response.setCreatedDate("Created On " + user.getCreatedDate().toString().substring(0, 10) + " at " + user.getCreatedDate().toString().substring(11, 19));
        response.setUpdatedDate("Last updated On " + user.getUpdatedDate().toString().substring(0, 10) + " at " + user.getUpdatedDate().toString().substring(11, 19));
        return response;
    }

    @Override
    public List<UserDTOResponse> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        List<UserDTOResponse> responses = new ArrayList<>();
        for (User u : allUsers) {
            UserDTOResponse response = new UserDTOResponse();
            BeanUtils.copyProperties(u, response);
            response.setCreatedDate("Created On " + u.getCreatedDate().toString().substring(0, 10) + " at " + u.getCreatedDate().toString().substring(11, 19));
            response.setUpdatedDate("Last updated On " + u.getUpdatedDate().toString().substring(0, 10) + " at " + u.getUpdatedDate().toString().substring(11, 19));
            responses.add(response);
        }
        return responses;
    }

    @Transactional
    @Override
    public UserDTOResponse deleteAUser(Long userId) {
        User user = findUserById(userId);
        validatePowers(user);

        Map<String, String> changes = new HashMap<>();
        changes.put("isUserDeleted", "true");
        userLogsService.createAChangeLogAndSave(userId, changes);

        UserDTOResponse response = new UserDTOResponse();
        BeanUtils.copyProperties(user, response);
        response.setCreatedDate("Created On " + user.getCreatedDate().toString().substring(0, 10) + " at " + user.getCreatedDate().toString().substring(11, 19));
        response.setUpdatedDate("Last updated On " + user.getUpdatedDate().toString().substring(0, 10) + " at " + user.getUpdatedDate().toString().substring(11, 19));
        passwordResetTokenRepository.deleteAllByForUser(user);
        userRepository.delete(user);
        return response;
    }

    @Override
    public UserDTOResponse updateRole(Long userId, String role) {
        User user = findUserById(userId);
        validatePowers(user);
        if (!role.equals("?")) {
            if (role.toUpperCase().contains("USER") && roleRepository.existsByRoleName(AppRole.ROLE_USER)) {
                user.setRole(roleRepository.findByRoleName(AppRole.ROLE_USER).orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER))));
            } else if (role.toUpperCase().contains("ADMIN") && roleRepository.existsByRoleName(AppRole.ROLE_ADMIN)) {
                user.setRole(roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN))));
            } else {
                user.setRole(roleRepository.findByRoleName(AppRole.ROLE_GUEST).orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_GUEST))));
            }
        }
        userRepository.updateUserById(user.getUserId(), user.getRole());
        UserDTOResponse response = new UserDTOResponse();
        BeanUtils.copyProperties(user, response);

        Map<String, String> changes = new HashMap<>();
        changes.put("isRoleUpdated", user.getRole().getRoleName().toString());
        userLogsService.createAChangeLogAndSave(userId, changes);

        response.setCreatedDate("Created On " + user.getCreatedDate().toString().substring(0, 10) + " at " + user.getCreatedDate().toString().substring(11, 19));
        response.setUpdatedDate("Last updated On " + user.getUpdatedDate().toString().substring(0, 10) + " at " + user.getUpdatedDate().toString().substring(11, 19));
        return response;
    }

    //Reset Password from Admin controller
    @Override
    public MessageResponse updatePassword(Long userId, String newPassword) {
        User user = findUserById(userId);
        validatePowers(user);

        Map<String, String> changes = new HashMap<>();
        changes.put("isPasswordUpdated", String.valueOf(true));
        userLogsService.createAChangeLogAndSave(userId, changes);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return new MessageResponse("Password Changed successfully",200);
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException("User doesn't exists", "USER_NOT_FOUND"));
    }


    @Override
    public MessageResponse updateAccountLockStatus(Long userId, boolean lock) {
        User user = findUserById(userId);
        validatePowers(user);
        user.setAccountNonLocked(!lock);
        userRepository.save(user);

        Map<String, String> changes = new HashMap<>();
        changes.put("isAccountNonLockedStatusUpdated", String.valueOf(true));
        userLogsService.createAChangeLogAndSave(userId, changes);

        return new MessageResponse("Account Lock Status changed to " + (lock ? "Locked" : "Unlocked"),200);
    }

    @Override
    public MessageResponse updateAccountExpiryStatus(Long userId, boolean expire) {
        User user = findUserById(userId);
        validatePowers(user);
        user.setAccountNonExpired(!expire);
        userRepository.save(user);
        Map<String, String> changes = new HashMap<>();
        changes.put("isAccountNonExpiredStatusUpdated", String.valueOf(true));
        userLogsService.createAChangeLogAndSave(userId, changes);
        return new MessageResponse("Account Expire Status changed to " + (expire ? "Expired" : "Not Expired"),200);
    }

    @Override
    public MessageResponse updateCredentialsExpiryStatus(Long userId, boolean expire) {
        User user = findUserById(userId);
        validatePowers(user);
        user.setCredentialsNonExpired(!expire);
        userRepository.save(user);
        Map<String, String> changes = new HashMap<>();
        changes.put("isCredentialsNonExpiredStatusUpdated", String.valueOf(true));
        userLogsService.createAChangeLogAndSave(userId, changes);
        return new MessageResponse("Account Credentials Status changed to " + (expire ? "Expired" : "Not Expired"),200);
    }

    @Override
    public MessageResponse updateAccountEnabledStatus(Long userId, boolean enabled) {
        User user = findUserById(userId);
        validatePowers(user);
        user.setEnabled(enabled);
        userRepository.save(user);
        Map<String, String> changes = new HashMap<>();
        changes.put("isEnabledStatusUpdated", String.valueOf(true));
        userLogsService.createAChangeLogAndSave(userId, changes);
        return new MessageResponse("Account status " + (enabled ? "Enabled " : "Disabled"),200);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    private void validatePowers(User user) {
        if (user.getRole().getRoleName().toString().contains("SU")) {
            throw new CustomException("AUK-AT mai, Super user h bo", "NO_OPERATION_ON_SUPER_USER");
        }
        String isSuperUser = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        if (!isSuperUser.toUpperCase().contains("SU")
                && user.getRole().getRoleName().toString().toUpperCase().contains("ADMIN")) {
            throw new CustomException("Admin doesn't have power modify Admin", "NO_POWERS");
        }
        return;
    }
}
