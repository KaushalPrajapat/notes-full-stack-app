package com.notes.notes_app.repos;

import com.notes.notes_app.models.NoteLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteLogsRepository extends JpaRepository<NoteLogs, Long> {
    List<NoteLogs> findAllByNoteOwner(String user);

    List<NoteLogs> findAllByChangedBy(String user);

    List<NoteLogs> findAllByNodeWhichGotChanged(Long nodeWhichGotChanged);

}
