package com.notes.notes_app.controllers;

import com.notes.notes_app.models.Note;
import com.notes.notes_app.security.request.SignupRequest;
import com.notes.notes_app.services.AdminService;
import com.notes.notes_app.services.AuthService;
import com.notes.notes_app.services.NoteService;
import com.notes.notes_app.services.UserLogsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/su")
//@CrossOrigin
public class SuperUserController {
    // What a super user can do
    // Literally He can do anything He want
    @Autowired
    private UserLogsService userLogsService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AuthService authService;

    @GetMapping("/check")
    public String checkSuper() {
        return "Returning from super User";
    }

    // --------------------------------------------<ADMIN-STYLE-WORK(CRUD on
    // user)>----------------------------------------//
    // Add new user, by admin
    @PostMapping("/add-user")
    public ResponseEntity<?> addUser(@Valid @RequestBody SignupRequest user) {
        return ResponseEntity.ok(authService.addUser(user));
    }

    // Get user by id, by admin
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getUser(userId));
    }

    // Get all users, by admin
    @GetMapping()
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    // Delete user, by admin
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.deleteAUser(userId));
    }

    // Update user-data role
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateRole(@PathVariable Long userId,
            @RequestParam(defaultValue = "?", name = "role") String role) {
        return ResponseEntity.ok(adminService.updateRole(userId, role));
    }

    // --------------------------------------------<SUPER-USER-WORK(Read operation
    // on userLogs)>----------------------------------------//
    // USER-LOGS
    @GetMapping("/all-logs")
    public ResponseEntity<?> getAllLogs() {
        return ResponseEntity.ok(userLogsService.getAllLogs());
    }

    @GetMapping("/all-logs-by-user")
    public ResponseEntity<?> getAllLogsByAUser(@RequestParam(name = "userId", defaultValue = "1") Long userId) {
        return ResponseEntity.ok(userLogsService.getAllLogsByAUser(userId));
    }

    @GetMapping("/all-logs-on-user")
    public ResponseEntity<?> getAllLogsOnAUser(@RequestParam(name = "userId", defaultValue = "1") Long userId) {
        return ResponseEntity.ok(userLogsService.getAllLogsOnAUser(userId));
    }

    // --------------------------------------------<GENERAL-WORK(RD on
    // Note)>----------------------------------------//
    @Autowired
    NoteService noteService;

    // Super-user NOTE relations
    // What can a super-user do;
    // He can check any note of any user with noteId only
    @GetMapping("/note")
    public ResponseEntity<?> getAllNotes4SuperUser() {
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    @GetMapping("/note/{noteId}")
    public ResponseEntity<?> getNoteByNoteId4SuperUser(@PathVariable(name = "noteId") Long noteId) {
        return ResponseEntity.ok(noteService.getNoteByNoteId(noteId));
    }

    // // PUT MAPPING UPDATE
    // @PutMapping("/note/{noteId}")
    // public ResponseEntity<?> updateNoteByNoteId4SuperUser(@PathVariable(name =
    // "noteId") Long noteId, @RequestBody Note note) {
    // return ResponseEntity.ok(noteService.updateAnyNoteNoteId(noteId,
    // note.getContent(), note.getNoteHeading()));
    // }

    @DeleteMapping("/note/{noteId}")
    public ResponseEntity<?> updateNoteByNoteId4SuperUser(@PathVariable(name = "noteId") Long noteId) {
        return ResponseEntity.ok(noteService.deleteAnyNoteByNoteId(noteId));
    }

}
