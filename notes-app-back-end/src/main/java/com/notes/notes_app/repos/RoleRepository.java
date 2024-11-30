package com.notes.notes_app.repos;

import com.notes.notes_app.models.AppRole;
import com.notes.notes_app.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);

    Boolean existsByRoleName(AppRole appRole);
}
