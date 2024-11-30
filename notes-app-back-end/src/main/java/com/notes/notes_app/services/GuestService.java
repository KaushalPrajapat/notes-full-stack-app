package com.notes.notes_app.services;

import com.notes.notes_app.security.response.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public interface GuestService {
    MessageResponse updatePassword(Long id, String newPassword);
}
