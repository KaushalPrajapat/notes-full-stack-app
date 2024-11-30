package com.notes.notes_app.services.Impl;

import com.notes.notes_app.models.User;
import com.notes.notes_app.repos.UserRepository;
import com.notes.notes_app.security.response.MessageResponse;
import com.notes.notes_app.services.GuestService;
import com.notes.notes_app.services.UserLogsService;
import com.notes.notes_app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GuestServiceImpl implements GuestService {
    @Autowired
    private UserLogsService userLogsService;
    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    //Reset Password from Admin controller
    @Override
    public MessageResponse updatePassword(Long userId, String newPassword) {
        User user = userService.findUserById(userId);
//        changedBy same user
//        ChangedOn same user
        Map<String, String> changes = new HashMap<>();
        changes.put("isPasswordUpdated", String.valueOf(true));
        userLogsService.createAChangeLogAndSave(userId, changes);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setShouldResetToken(!user.isShouldResetToken());
        userRepository.save(user);
        return new MessageResponse("Password Changed successfully",200);
    }
}
