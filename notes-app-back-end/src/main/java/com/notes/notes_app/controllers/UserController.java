package com.notes.notes_app.controllers;

import com.notes.notes_app.models.Note;
import com.notes.notes_app.security.response.MessageResponse;
import com.notes.notes_app.services.NoteService;
import com.notes.notes_app.services.UserService;
import com.notes.notes_app.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
//@CrossOrigin
public class UserController {
    @Autowired
    private AuthUtils authUtils;
    @Autowired
    private UserService userService;

    @GetMapping("/check")
    public String check() {
        return "This thing I'm returning from user controller";
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails() {
        return ResponseEntity.ok(authUtils.loggedInUser());
    }

    @GetMapping("/username")
    public String currentUserName() {
        return (authUtils.loggedInUser() != null) ? authUtils.loggedInUser().getUsername() : "";
    }

    // I'm using authService methods here, Bz auth service and User Service reset is
    // same
    // Only difference is from user-service he has to have user authorities to
    // change password
    // Or a user change his password without any validation needs -
    // Which one should I use
    @PutMapping("/update-password")
    public ResponseEntity<MessageResponse> updatePassword(@RequestParam String newPassword) {
        return ResponseEntity.ok(userService.updatePassword(authUtils.loggedInUserId(), newPassword));
    }

    @Autowired
    NoteService noteService;

    // Super-user NOTE relations
    // What can a super-user do;
    // He can check any note of any user with noteId only
    @PostMapping("/note")
    public ResponseEntity<?> addANote4LoggedInUser(@RequestBody(required = true) Note note) {
        return ResponseEntity.ok(noteService.addNote(authUtils.loggedInUser(), note));
    }
//-----------------------------------------------------
    @GetMapping("/note")
    public ResponseEntity<?> getAllToLoggedInUser() {
        return ResponseEntity.ok(noteService.getAllNotes4LoggedInUser(authUtils.loggedInUser()));
    }

    @GetMapping("/note/{noteId}")
    public ResponseEntity<?> getNoteByNoteId4SuperUser(@PathVariable(name = "noteId") Long noteId) {
        return ResponseEntity.ok(noteService.getNoteByNoteId4LoggedInUser(authUtils.loggedInUser(), noteId));
    }

    // PUT MAPPING UPDATE
    @PutMapping("/note/{noteId}")
    public ResponseEntity<?> updateNoteByNoteId4SuperUser(@PathVariable(name = "noteId") Long noteId,
            @RequestBody Note note) {
        return ResponseEntity.ok(noteService.updateNoteOfLoggedInUserByNoteId(authUtils.loggedInUser(), noteId,
                note.getContent(), note.getNoteHeading()));
    }

    @DeleteMapping("/note/{noteId}")
    public ResponseEntity<?> updateNoteByNoteId4SuperUser(@PathVariable(name = "noteId") Long noteId) {
        return ResponseEntity.ok(noteService.deleteNodeOfLoggedInUserByNoteId(authUtils.loggedInUser(), noteId));
    }

}
