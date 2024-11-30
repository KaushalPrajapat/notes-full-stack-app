package com.notes.notes_app.controllers;

import com.notes.notes_app.dtos.NoteDto;
import com.notes.notes_app.models.Note;
import com.notes.notes_app.services.NoteService;
import com.notes.notes_app.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/note")
//@CrossOrigin
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private AuthUtils authUtils;

    @GetMapping("/check")
    public String check() {
        return "This thing I'm returning from Notes controller";
    }

    @PostMapping("/add-note")
    public ResponseEntity<NoteDto> addNote(@RequestBody Note note) {
        return ResponseEntity.ok(noteService.addNote(authUtils.loggedInUser(), note));
    }

    @GetMapping()
    public ResponseEntity<?> getAllNotesOfUser() {
        return ResponseEntity.ok(noteService.getAllNotes4LoggedInUser(authUtils.loggedInUser()));
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<?> getNoteOfUserByNoteId(
            @PathVariable Long noteId) {
        return ResponseEntity.ok(noteService.getNoteByNoteId4LoggedInUser(authUtils.loggedInUser(), noteId));
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<?> updateNoteOfUserByNoteId(@PathVariable Long noteId,
            @RequestBody Note note) {
        return ResponseEntity.ok(noteService.updateNoteOfLoggedInUserByNoteId(authUtils.loggedInUser(), noteId,
                note.getContent(), note.getNoteHeading()));
        // Only saving content
        // That why only using content field , not any-other field
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<?> deleteNoteOfUserByNoteId(@PathVariable Long noteId) {
        return ResponseEntity.ok(noteService.deleteNodeOfLoggedInUserByNoteId(authUtils.loggedInUser(), noteId));
    }
}
