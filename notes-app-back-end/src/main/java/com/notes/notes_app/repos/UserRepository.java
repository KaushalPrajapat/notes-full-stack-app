package com.notes.notes_app.repos;

import com.notes.notes_app.models.Role;
import com.notes.notes_app.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);

    Boolean existsByEmailOrUsername(String email, String username);

    Boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.role = :role WHERE u.id = :userId")
    void updateUserById(@Param("userId") Long userId,
                       @Param("role") Role role);

}
