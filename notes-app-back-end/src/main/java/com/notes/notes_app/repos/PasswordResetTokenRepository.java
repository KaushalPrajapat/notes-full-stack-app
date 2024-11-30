package com.notes.notes_app.repos;

import com.notes.notes_app.models.PasswordResetToken;
import com.notes.notes_app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    void deleteAllByForUser(User user);
}
