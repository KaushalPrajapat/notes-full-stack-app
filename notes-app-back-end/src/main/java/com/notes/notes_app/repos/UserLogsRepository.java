package com.notes.notes_app.repos;

import com.notes.notes_app.models.User;
import com.notes.notes_app.models.UserLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLogsRepository extends JpaRepository<UserLogs, Long> {
    List<UserLogs> findByChangedBy(User user);

    List<UserLogs> findByChangedOn(Long userId);
}
