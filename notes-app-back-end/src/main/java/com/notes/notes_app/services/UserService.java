package com.notes.notes_app.services;

import com.notes.notes_app.dtos.NoteDto;
import com.notes.notes_app.models.Role;
import com.notes.notes_app.models.User;
import com.notes.notes_app.security.response.MessageResponse;
import jakarta.mail.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    User findByUserName(String username);

    User findUserById(long userId);

    Optional<User> findUserByEmail(String email);

    User findUserByUserName(String username);

    User registerUser(User user);

    MessageResponse updatePassword(Long id, String newPassword);

}
