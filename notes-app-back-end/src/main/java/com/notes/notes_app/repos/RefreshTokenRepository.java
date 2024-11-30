package com.notes.notes_app.repos;

import com.notes.notes_app.models.RefreshToken;
import com.notes.notes_app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String token);

    boolean existsByUser(User user);

    RefreshToken findByUser(User user);
}
