package com.notes.notes_app.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.notes.notes_app.security.response.MessageResponse;
import com.notes.notes_app.services.UserLogsService;
import com.notes.notes_app.exceptions.CustomException;
import com.notes.notes_app.models.User;
import com.notes.notes_app.repos.PasswordResetTokenRepository;
import com.notes.notes_app.repos.RoleRepository;
import com.notes.notes_app.repos.UserRepository;
import com.notes.notes_app.services.UserService;
import com.notes.notes_app.utils.EmailUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private EmailUtils emailUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserLogsService userLogsService;

    @Override
    public User findByUserName(String username) {
        return findUserByUserName(username);
    }


    @Override
    public User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException("User doesn't exists with userId " + userId, "USER_NOT_FOUND"));
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserByUserName(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new CustomException("User doesn't exists with username " + username, "USER_NOT_FOUND"));
    }

    @Override
    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            user.setUsername(user.getUsername() + UUID.randomUUID());
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            user.setEmail(user.getEmail() + UUID.randomUUID());
        }
        if (user.getPassword() != null)
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    //Reset Password user's own password
    @Override
    public MessageResponse updatePassword(Long userId, String newPassword) {
        User user = findUserById(userId);
        Map<String, String> changes = new HashMap<>();
        changes.put("isPasswordUpdated", String.valueOf(true));
        userLogsService.createAChangeLogAndSave(userId, changes);
        user.setShouldResetToken(!user.isShouldResetToken());
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return new MessageResponse("Password Changed successfully",200);
    }


}
