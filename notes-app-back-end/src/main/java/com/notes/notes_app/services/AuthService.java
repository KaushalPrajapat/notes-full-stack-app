package com.notes.notes_app.services;

import com.notes.notes_app.security.request.RefreshTokenRequest;
import com.notes.notes_app.security.response.MessageResponse;
import org.springframework.stereotype.Service;

import com.notes.notes_app.dtos.UserDTOResponse;
import com.notes.notes_app.security.request.LoginRequest;
import com.notes.notes_app.security.request.SignupRequest;
import com.notes.notes_app.security.response.LoginResponse;
import com.notes.notes_app.security.service.UserDetailsImpl;

@Service
public interface AuthService {
    LoginResponse signin(LoginRequest request);
    UserDTOResponse addUser(SignupRequest signUpRequest);
    UserDTOResponse getUserDetails(UserDetailsImpl userDetails);

    MessageResponse generatePasswordResetToken(String email);

    MessageResponse resetPassword(String token, String newPassword);

    //  After creating account, by user throw public api he needs to validate his account
    MessageResponse validateUser(String token);

    LoginResponse getMeRefreshToken(RefreshTokenRequest refreshTokenRequest);
}
