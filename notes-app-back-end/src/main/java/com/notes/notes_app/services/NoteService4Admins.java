package com.notes.notes_app.services;

import com.notes.notes_app.dtos.NoteDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService4Admins {
    List<NoteDto> getAllNotes();

    NoteDto getNoteByNoteId(Long noteId);

    @Transactional
    NoteDto updateAnyNoteNoteId(Long noteId, String content, String noteHeading);

    @Transactional
    NoteDto deleteAnyNoteByNoteId(Long noteId);
}
