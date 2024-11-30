package com.notes.notes_app.services;


import com.notes.notes_app.dtos.NoteLogsDto;
import com.notes.notes_app.models.Note;
import com.notes.notes_app.models.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteLogsService {

    List<NoteLogsDto> getAllNotesLogs4AllUsers();

    List<NoteLogsDto> getAllNotesLogs4AUsers();

    List<NoteLogsDto> getAllNotesLogsChanged4AUsers();


    @Transactional
    void createANoteChangeLogAndSave(String oldContent, String Content, User currentloggedInUser, String noteOwner, Note note, String newNoteHeading, String oldNoteHeading);

    List<NoteLogsDto> getLogsForNoteByNoteId(Long noteId);

    List<NoteLogsDto> getAllNotesLogsByAUsers();

    List<NoteLogsDto> getLogsOfANoteOfAnyUsers(Long noteId);
}
