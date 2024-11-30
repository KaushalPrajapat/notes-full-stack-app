package com.notes.notes_app.controllers;


import com.notes.notes_app.services.NoteLogsService;
import com.notes.notes_app.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notes/log")
//@CrossOrigin
public class NoteLogsController {
    @Autowired
    private NoteLogsService noteLogsService;
    @Autowired
    private AuthUtils authUtils;

    @GetMapping("/check")
    public String checkNoteLogs() {
        return "Returning from note logs check place";
    }

    //Whole dumps for user's notes change logs
    @PreAuthorize("hasRole('ROLE_SU') || hasRole('ROLE_ADMIN')")
    @GetMapping("/admin") //get all note logs for superuser, he can filter based on note id or user id also
    public ResponseEntity<?> getAllNotesLogs4AllUsers() {
        return ResponseEntity.ok(noteLogsService.getAllNotesLogs4AllUsers());
    }

    @PreAuthorize("hasRole('ROLE_SU') || hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/{noteId}") //get all note logs for superuser, he can filter based on note id or user id also
    public ResponseEntity<?> getLogsOfANoteOfAnyUsers(@PathVariable(name = "noteId") Long noteId) {
        return ResponseEntity.ok(noteLogsService.getLogsOfANoteOfAnyUsers(noteId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/on") // all logs based on owner, Like meri cheejo ko kisane chheda
    public ResponseEntity<?> getAllNotesLogs4AUsers() {  // mere notes k chhedchhad
        return ResponseEntity.ok(noteLogsService.getAllNotesLogs4AUsers());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/by") // all logs based on owner, Like meri cheejo ko kisane chheda
    public ResponseEntity<?> getAllNotesLogsByAUsers() {  // mere notes k chhedchhad
        return ResponseEntity.ok(noteLogsService.getAllNotesLogsByAUsers());
    }

    @GetMapping("/{noteId}") // who-ever changed my note, show those logs means logs by onwWhichNote...
    public ResponseEntity<?> getLogsForNoteByNoteId4(@PathVariable Long noteId) {
        return ResponseEntity.ok(noteLogsService.getLogsForNoteByNoteId(noteId));
    }

}
