package com.notes.notes_app.repos;

import com.notes.notes_app.models.Note;
import com.notes.notes_app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByNoteOwner(User userById);
}
