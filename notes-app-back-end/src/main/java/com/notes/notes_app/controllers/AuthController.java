package com.notes.notes_app.controllers;

import com.notes.notes_app.exceptions.CustomException;
import com.notes.notes_app.security.request.LoginRequest;
import com.notes.notes_app.security.request.RefreshTokenRequest;
import com.notes.notes_app.security.request.SignupRequest;
import com.notes.notes_app.services.AuthService;
import com.notes.notes_app.services.UserService;
import com.notes.notes_app.utils.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth/public")
//@CrossOrigin
public class AuthController {
    @Autowired
    private AuthUtils authUtils;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @GetMapping("/check")
    public String check() {

        return "ResponseEntity.ok(authService.signin(loginRequest))";
    }
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
//        System.out.println(loginRequest);
            return ResponseEntity.ok(authService.signin(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return ResponseEntity.ok(authService.addUser(signUpRequest));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails() {
        try {
            return ResponseEntity.ok(authUtils.loggedInUser());
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), "Please log in first");
        }
    }
    @GetMapping("/validate-user")
    public ResponseEntity<?> validateUser(@RequestParam(name = "token") String token) {
        return ResponseEntity.ok(authService.validateUser(token));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam(name = "email") String email) {
        return ResponseEntity.ok(authService.generatePasswordResetToken(email));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam(name = "token") String token,
                                           @RequestParam(name = "newPassword") String newPassword) {
        return ResponseEntity.ok(authService.resetPassword(token, newPassword));
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
//        System.out.println(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.ok(authService.getMeRefreshToken(refreshTokenRequest));
    }
}

