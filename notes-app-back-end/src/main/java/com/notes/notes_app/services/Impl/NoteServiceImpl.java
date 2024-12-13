package com.notes.notes_app.services.Impl;

import com.notes.notes_app.dtos.NoteDto;
import com.notes.notes_app.exceptions.CustomException;
import com.notes.notes_app.models.Note;
import com.notes.notes_app.models.User;
import com.notes.notes_app.repos.NoteRepository;
import com.notes.notes_app.services.NoteLogsService;
import com.notes.notes_app.services.NoteService;
import com.notes.notes_app.utils.AuthUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class NoteServiceImpl implements NoteService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteLogsService noteLogsService;

    @Autowired
    private AuthUtils authUtils;

    @Override
    public NoteDto addNote(User createdBy, Note note) {
        note.setNoteOwner(createdBy.getUsername());
        Note savednote = noteRepository.save(note);
        NoteDto noteDto = new NoteDto();
        BeanUtils.copyProperties(savednote, noteDto);
        //noteDto.setNoteHeading(sa);
        noteDto.setNoteOwner(createdBy.getUsername());
        noteDto.setCreatedDate(savednote.getCreatedDate().toString().substring(0, 10) + " at " + savednote.getCreatedDate().toString().substring(11, 19));
        noteDto.setUpdatedDate(savednote.getUpdatedDate().toString().substring(0, 10) + " at " + savednote.getUpdatedDate().toString().substring(11, 19));
        noteLogsService.createANoteChangeLogAndSave("CREATE", savednote.getContent(), createdBy, savednote.getNoteOwner(), savednote, savednote.getNoteHeading(), "CREATE");
        return noteDto;
    }


    @Transactional
    @Override
    public List<NoteDto> getAllNotes4LoggedInUser(User user) {
        String query = "SELECT d FROM Note d WHERE d.noteOwner = :noteOwner";
        List<Note> listNote4User = entityManager.createQuery(query, Note.class)
                .setParameter("noteOwner", user.getUsername())  // Setting the parameter in the query
                .getResultList();// Returns a list of documents that match the condition
        List<NoteDto> results = new ArrayList<>();
        for (var note : listNote4User) {
//            System.out.println(note.getContent());
            NoteDto noteDto = new NoteDto();
            BeanUtils.copyProperties(note, noteDto);
            noteDto.setNoteOwner(note.getNoteOwner());
            noteDto.setCreatedDate(note.getCreatedDate().toString().substring(0, 19));
            noteDto.setUpdatedDate(note.getUpdatedDate().toString().substring(0, 19));
            results.add(noteDto);
        }
        return results;
    }

    @Transactional
    @Override
    public NoteDto getNoteByNoteId4LoggedInUser(User user, Long noteId) {
        String query = "SELECT d FROM Note d WHERE d.noteOwner = :noteOwner AND d.noteId = :noteId";
        List<Note> note4UserByNoteId = entityManager.createQuery(query, Note.class)
                .setParameter("noteOwner", user.getUsername())  // Setting the parameter in the query
                .setParameter("noteId", noteId)
                .getResultList();
        if (note4UserByNoteId.isEmpty()) {
            throw new CustomException("No note exist with id " + noteId + " and owner " + user.getUsername() + ".", "NOTE_NOT_FOUND");
        }
        //Only one result is possible - No need to loop also but, as I'm collected results in List so doing this shit
        NoteDto noteDto = new NoteDto();
        for (var note : note4UserByNoteId) {
//            System.out.println(note.getContent());
            BeanUtils.copyProperties(note, noteDto);
            noteDto.setNoteOwner(note.getNoteOwner());
            noteDto.setCreatedDate( note.getCreatedDate().toString().substring(0, 10) + " at " + note.getCreatedDate().toString().substring(11, 19));
            noteDto.setUpdatedDate(note.getUpdatedDate().toString().substring(0, 10) + " at " + note.getUpdatedDate().toString().substring(11, 19));

        }
        return noteDto;
    }

    @Transactional
    @Override
    public NoteDto updateNoteOfLoggedInUserByNoteId(User user, Long noteId, String content, String noteHeading) {
        String query = "SELECT d FROM Note d WHERE d.noteOwner = :noteOwner AND d.noteId = :noteId";
        List<Note> note4UserByNoteId = entityManager.createQuery(query, Note.class)
                .setParameter("noteOwner", user.getUsername())  // Setting the parameter in the query
                .setParameter("noteId", noteId)
                .getResultList();
        if (note4UserByNoteId.isEmpty()) {
            throw new CustomException("No note exist with id " + noteId + " and owner " + user.getUsername() + ".", "NOTE_NOT_FOUND");
        }
        //Only one result is possible - No need to loop also but, as I'm collected results in List so doing this shit
        NoteDto noteDto = new NoteDto();
        for (var note : note4UserByNoteId) {
            var oldContent = note.getContent();
            var oldHeading = note.getNoteHeading();
            if (!noteHeading.isEmpty()) {
                note.setNoteHeading(noteHeading);
            }
            if (!content.isEmpty()) {
                note.setContent(content);
            }
            BeanUtils.copyProperties(note, noteDto);
            noteDto.setNoteOwner(note.getNoteOwner());
            noteDto.setCreatedDate(note.getCreatedDate().toString().substring(0, 19));
            noteDto.setUpdatedDate(LocalDateTime.now().toString().substring(0, 19));
            //Create log
            noteLogsService.createANoteChangeLogAndSave(oldContent, !content.isEmpty() ? content : "", user, note.getNoteOwner(), note, !noteHeading.isEmpty() ? noteHeading : "", oldHeading);

            noteRepository.save(note);
        }
        return noteDto;
    }

    @Transactional
    @Override
    public NoteDto deleteNodeOfLoggedInUserByNoteId(User user, Long noteId) {
        String query = "SELECT d FROM Note d WHERE d.noteOwner = :noteOwner AND d.noteId = :noteId";
        List<Note> note4UserByNoteId = entityManager.createQuery(query, Note.class)
                .setParameter("noteOwner", user.getUsername())
                .setParameter("noteId", noteId)
                .getResultList();
        if (note4UserByNoteId.isEmpty()) {
            throw new CustomException("No note exist with id " + noteId + " and owner " + user.getUsername() + ".", "NOTE_NOT_FOUND");
        }
        //Only one result is possible - No need to loop also but, as I'm collected results in List so doing this shit
        NoteDto noteDto = new NoteDto();
        for (var note : note4UserByNoteId) {
            //  System.out.println(note.getContent());
            BeanUtils.copyProperties(note, noteDto);
            noteDto.setNoteOwner(note.getNoteOwner());
            noteDto.setCreatedDate(note.getCreatedDate().toString().substring(0, 19));
            noteDto.setUpdatedDate(LocalDateTime.now().toString().substring(0, 19));

            noteLogsService.createANoteChangeLogAndSave(note.getContent(), "DELETE", user, note.getNoteOwner(), note, "DELETE", note.getNoteHeading());

            noteRepository.delete(note);
        }
        return noteDto;
    }


    @Override
    public List<NoteDto> getAllNotes() {
        List<Note> listNote = noteRepository.findAll();
        List<NoteDto> results = new ArrayList<>();
        for (var note : listNote) {
//            System.out.println(note.getContent());
            NoteDto noteDto = new NoteDto();
            BeanUtils.copyProperties(note, noteDto);
            noteDto.setNoteOwner(note.getNoteOwner());
            noteDto.setCreatedDate(note.getCreatedDate().toString().substring(0, 19));
            noteDto.setUpdatedDate(note.getUpdatedDate().toString().substring(0, 19));
            results.add(noteDto);
        }
        return results;
    }

    @Override
    public NoteDto getNoteByNoteId(Long noteId) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new CustomException("No note exist with id " + noteId, "NOTE_NOT_FOUND"));

        //Only one result is possible - No need to loop also but, as I'm collected results in List so doing this shit
        NoteDto noteDto = new NoteDto();
//        System.out.println(note.getContent());
        BeanUtils.copyProperties(note, noteDto);
        noteDto.setNoteOwner(note.getNoteOwner());
        noteDto.setCreatedDate(note.getCreatedDate().toString().substring(0, 10) + " at " + note.getCreatedDate().toString().substring(11, 19));
        noteDto.setUpdatedDate(note.getUpdatedDate().toString().substring(0, 10) + " at " + note.getUpdatedDate().toString().substring(11, 19));

        return noteDto;
    }

    @Transactional
    @Override
    public NoteDto updateAnyNoteNoteId(Long noteId, String content, String noteHeading) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new CustomException("No note exist with id " + noteId, "NOTE_NOT_FOUND"));

        //Only one result is possible - No need to loop also but, as I'm collected results in List so doing this shit
        NoteDto noteDto = new NoteDto();
        var oldContent = note.getContent();
        var oldHeading = note.getNoteHeading();
        if (!noteHeading.isEmpty()) {
            note.setNoteHeading(noteHeading);
        }
        if (!content.isEmpty()) {
            note.setContent(content);
        }
        BeanUtils.copyProperties(note, noteDto);
        noteDto.setNoteOwner(note.getNoteOwner());
        noteDto.setCreatedDate(note.getCreatedDate().toString().substring(0, 19));
        noteDto.setUpdatedDate(LocalDateTime.now().toString().substring(0, 19));
        noteRepository.save(note);
        //Create log
        noteLogsService.createANoteChangeLogAndSave(oldContent, !content.isEmpty() ? content : "", authUtils.loggedInUser(), note.getNoteOwner(), note, !noteHeading.isEmpty() ? noteHeading : "", oldHeading);

        return noteDto;
    }

    @Transactional
    @Override
    public NoteDto deleteAnyNoteByNoteId(Long noteId) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new CustomException("Note doesn't exists", "NO_NOTE_WITH_ID"));
        NoteDto noteDto = new NoteDto();
        BeanUtils.copyProperties(note, noteDto);
        noteDto.setNoteOwner(note.getNoteOwner());
        noteDto.setCreatedDate(note.getCreatedDate().toString().substring(0, 10) + " at " + note.getCreatedDate().toString().substring(11, 19));
        noteDto.setUpdatedDate(note.getUpdatedDate().toString().substring(0, 10) + " at " + note.getUpdatedDate().toString().substring(11, 19));
        noteLogsService.createANoteChangeLogAndSave(note.getContent(), "DELETE", authUtils.loggedInUser(), note.getNoteOwner(), note, "DELETE", note.getNoteHeading());
        noteRepository.deleteById(noteId);
        return noteDto;
    }

}
