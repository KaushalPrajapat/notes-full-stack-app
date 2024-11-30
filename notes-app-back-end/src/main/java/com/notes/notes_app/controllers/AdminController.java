package com.notes.notes_app.controllers;

import com.notes.notes_app.models.Note;
import com.notes.notes_app.models.Role;
import com.notes.notes_app.security.request.SignupRequest;
import com.notes.notes_app.security.response.MessageResponse;
import com.notes.notes_app.services.AdminService;
import com.notes.notes_app.services.AuthService;
import com.notes.notes_app.services.Impl.NoteServiceImpl;
import com.notes.notes_app.utils.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin")
//@CrossOrigin
public class AdminController {
    //    Autowired
    @Autowired
    private AdminService adminService;
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthUtils authUtils;
    //ADMIN NOTE relations



    @GetMapping("/check")
    public String check() {
        return "This thing I'm returning from Admin controller";
    }

    //Add new user, by admin
    @PostMapping("/add-user")
    public ResponseEntity<?> addUser(@Valid @RequestBody SignupRequest user) {
        return ResponseEntity.ok(authService.addUser(user));
    }

    //Get user by id, by admin
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getUser(userId));
    }

    //Get all users, by admin
    @GetMapping()
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    //Delete user, by admin
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.deleteAUser(userId));
    }

    //    Update user-data role
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateRole(@PathVariable Long userId,
                                        @RequestParam(defaultValue = "?", name = "role") String role) {
        return ResponseEntity.ok(adminService.updateRole(userId, role));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails() {
        return ResponseEntity.ok(authUtils.loggedInUser());
    }

    @GetMapping("/username")
    public String currentUserName() {
        return (authUtils.loggedInUser() != null) ? authUtils.loggedInUser().getUsername() : "";
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestParam Long userId,
                                            @RequestParam(name = "newPassword") String newPassword) {
        return ResponseEntity.ok(adminService.updatePassword(userId, newPassword));
    }

    @PutMapping("/update-lock-status")
    public ResponseEntity<MessageResponse> updateAccountLockStatus(@RequestParam Long userId,
                                                                   @RequestParam boolean lock) {

        return ResponseEntity.ok(adminService.updateAccountLockStatus(userId, lock));
    }

    @PutMapping("/update-expiry-status")
    public ResponseEntity<MessageResponse> updateAccountExpiryStatus(@RequestParam Long userId,
                                                                     @RequestParam boolean expire) {

        return ResponseEntity.ok(adminService.updateAccountExpiryStatus(userId, expire));
    }

    @PutMapping("/update-enabled-status")
    public ResponseEntity<MessageResponse> updateAccountEnabledStatus(@RequestParam Long userId,
                                                                      @RequestParam boolean enabled) {
        return ResponseEntity.ok(adminService.updateAccountEnabledStatus(userId, enabled));
    }

    @PutMapping("/update-credentials-expiry-status")
    public ResponseEntity<MessageResponse> updateCredentialsExpiryStatus(@RequestParam Long userId, @RequestParam boolean expire) {

        return ResponseEntity.ok(adminService.updateCredentialsExpiryStatus(userId, expire));
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return adminService.getAllRoles();
    }



    @Autowired
    private NoteServiceImpl noteService;
    //    What can a admin do;
    //    He can check any note of any user with noteId only
    @GetMapping("/note")
    public ResponseEntity<?> getAllNotes4Admin() {
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    @GetMapping("/note/{noteId}")
    public ResponseEntity<?> getNoteByNoteId4Admin(@PathVariable(name = "noteId") Long noteId) {
        return ResponseEntity.ok(noteService.getNoteByNoteId(noteId));
    }

    // PUT MAPPING UPDATE
    @PutMapping("/note/{noteId}")
    public ResponseEntity<?> updateNoteByNoteId4Admin(@PathVariable(name = "noteId") Long noteId, @RequestBody Note note) {
        return ResponseEntity.ok(noteService.updateAnyNoteNoteId(noteId, note.getContent(), note.getNoteHeading()));
    }

    @DeleteMapping("/note/{noteId}")
    public ResponseEntity<?> updateNoteByNoteId4Admin(@PathVariable(name = "noteId") Long noteId) {
        return ResponseEntity.ok(noteService.deleteAnyNoteByNoteId(noteId));
    }
}

