package com.notes.notes_app.services.Impl;

import com.notes.notes_app.dtos.NoteLogsDto;
import com.notes.notes_app.exceptions.CustomException;
import com.notes.notes_app.models.Note;
import com.notes.notes_app.models.NoteLogs;
import com.notes.notes_app.models.User;
import com.notes.notes_app.repos.NoteLogsRepository;
import com.notes.notes_app.repos.NoteRepository;
import com.notes.notes_app.services.NoteLogsService;
import com.notes.notes_app.utils.AuthUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteLogsServiceImpl implements NoteLogsService {
    @Autowired
    NoteLogsRepository noteLogsRepository;
    @Autowired
    private AuthUtils authUtils;
    @Autowired
    NoteRepository noteRepository;

    @Override
    public List<NoteLogsDto> getAllNotesLogs4AllUsers() {
        List<NoteLogsDto> results = new ArrayList<>();
        List<NoteLogs> allNotesLogs = noteLogsRepository.findAll();
        for (var noteLog : allNotesLogs) {
            NoteLogsDto tempDto = new NoteLogsDto();
            BeanUtils.copyProperties(noteLog, tempDto);
            tempDto.setNoteOwner(noteLog.getNoteOwner());
            tempDto.setChangedBy(noteLog.getChangedBy());
            tempDto.setCreatedDate(noteLog.getCreatedDate().toString().substring(0, 10) + " at " + noteLog.getCreatedDate().toString().substring(11, 19));
            results.add(tempDto);
        }
        return results;
    }

    @Override
    public List<NoteLogsDto> getAllNotesLogs4AUsers() {
        List<NoteLogsDto> results = new ArrayList<>();
        List<NoteLogs> allNotesLogs = noteLogsRepository.findAllByNoteOwner(authUtils.loggedInUser().getUsername());//noteOwner
        for (var noteLog : allNotesLogs) {
            NoteLogsDto tempDto = new NoteLogsDto();
            BeanUtils.copyProperties(noteLog, tempDto);
            tempDto.setNoteOwner(noteLog.getNoteOwner());
            tempDto.setChangedBy(noteLog.getChangedBy());
            tempDto.setCreatedDate(noteLog.getCreatedDate().toString().substring(0, 10) + " at " + noteLog.getCreatedDate().toString().substring(11, 19));
            results.add(tempDto);
        }
        return results;
    }

    @Override
    public List<NoteLogsDto> getAllNotesLogsChanged4AUsers() {
        List<NoteLogsDto> results = new ArrayList<>();
        List<NoteLogs> allNotesLogs = noteLogsRepository.findAllByChangedBy(authUtils.loggedInUser().getUsername());//changedBy
        for (var noteLog : allNotesLogs) {
            NoteLogsDto tempDto = new NoteLogsDto();
            BeanUtils.copyProperties(noteLog, tempDto);
            tempDto.setNoteOwner(noteLog.getNoteOwner());
            tempDto.setChangedBy(noteLog.getChangedBy());
            tempDto.setCreatedDate(noteLog.getCreatedDate().toString().substring(0, 10) + " at " + noteLog.getCreatedDate().toString().substring(11, 19));
            results.add(tempDto);
        }
        return results;
    }

    @Override
    public List<NoteLogsDto> getLogsForNoteByNoteId(Long noteId) {
        Note isAuthrizedUserToNote = noteRepository.findById(noteId).orElseThrow(() -> new CustomException("Note doesn't exists with given Id", "NO_NOTE"));
        if (!isAuthrizedUserToNote.getNoteOwner().equalsIgnoreCase(authUtils.loggedInUser().getUsername())) {
            throw new CustomException("You are not authorize to access this note", "NOT_VALID_AUTH");
        }
        List<NoteLogsDto> results = new ArrayList<>();
        List<NoteLogs> allNotesLogs = noteLogsRepository.findAllByNodeWhichGotChanged(noteId);//changedBy
        for (var noteLog : allNotesLogs) {
            NoteLogsDto tempDto = new NoteLogsDto();
            BeanUtils.copyProperties(noteLog, tempDto);
            tempDto.setNoteOwner(noteLog.getNoteOwner());
            tempDto.setChangedBy(noteLog.getChangedBy());
            tempDto.setCreatedDate(noteLog.getCreatedDate().toString().substring(0, 10) + " at " + noteLog.getCreatedDate().toString().substring(11, 19));
            results.add(tempDto);
        }
        return results;
    }

    @Override
    public List<NoteLogsDto> getAllNotesLogsByAUsers() {
        List<NoteLogsDto> results = new ArrayList<>();
        List<NoteLogs> allNotesLogs = noteLogsRepository.findAllByChangedBy(authUtils.loggedInUser().getUsername());//noteOwner
        for (var noteLog : allNotesLogs) {
            NoteLogsDto tempDto = new NoteLogsDto();
            BeanUtils.copyProperties(noteLog, tempDto);
            tempDto.setNoteOwner(noteLog.getNoteOwner());
            tempDto.setChangedBy(noteLog.getChangedBy());
            tempDto.setCreatedDate(noteLog.getCreatedDate().toString().substring(0, 10) + " at " + noteLog.getCreatedDate().toString().substring(11, 19));
            results.add(tempDto);
        }
        return results;
    }

    @Transactional
    @Override
    public void createANoteChangeLogAndSave(String oldContent, String newContent, User currentloggedInUser, String noteOwner, Note note, String newNoteHeading, String oldNoteHeading) {
        NoteLogs noteLogs = new NoteLogs();
        noteLogs.setNewHeading(newNoteHeading);
        noteLogs.setOldHeading(oldNoteHeading);
        noteLogs.setOldContent(oldContent);
        noteLogs.setNewContent(newContent);
        noteLogs.setNoteOwner(noteOwner);
        noteLogs.setChangedBy(currentloggedInUser.getUsername());
        noteLogs.setNodeWhichGotChanged(note.getNoteId());
        noteLogsRepository.save(noteLogs);
    }

    @Override
    public List<NoteLogsDto> getLogsOfANoteOfAnyUsers(Long noteId) {
        List<NoteLogsDto> results = new ArrayList<>();
        List<NoteLogs> allNotesLogs = noteLogsRepository.findAllByNodeWhichGotChanged(noteId);//changedBy
        for (var noteLog : allNotesLogs) {
            NoteLogsDto tempDto = new NoteLogsDto();
            BeanUtils.copyProperties(noteLog, tempDto);
            tempDto.setNoteOwner(noteLog.getNoteOwner());
            tempDto.setChangedBy(noteLog.getChangedBy());
            tempDto.setCreatedDate(noteLog.getCreatedDate().toString().substring(0, 10) + " at " + noteLog.getCreatedDate().toString().substring(11, 19));
            results.add(tempDto);
        }
        return results;
    }

}
