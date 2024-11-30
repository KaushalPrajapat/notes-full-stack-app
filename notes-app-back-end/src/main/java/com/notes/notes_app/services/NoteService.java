package com.notes.notes_app.services;


import com.notes.notes_app.dtos.NoteDto;
import com.notes.notes_app.models.Note;
import com.notes.notes_app.models.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService {
    NoteDto addNote(User createdBy, Note note);

    List<NoteDto> getAllNotes4LoggedInUser(User loggedInuser);

    @Transactional
    NoteDto getNoteByNoteId4LoggedInUser(User user, Long noteId);

    @Transactional
    NoteDto updateNoteOfLoggedInUserByNoteId(User user, Long noteId, String content, String noteHeading);

    @Transactional
    NoteDto deleteNodeOfLoggedInUserByNoteId(User user, Long noteId);


    //  For admin and superuser only

    List<NoteDto> getAllNotes();

    NoteDto getNoteByNoteId(Long noteId);

    @Transactional
    NoteDto updateAnyNoteNoteId(Long noteId, String content, String noteHeading);

    @Transactional
    NoteDto deleteAnyNoteByNoteId(Long noteId);

}
