package com.notes.notes_app.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Data
public class SignupRequest {
    @NotBlank
    @Size(max = 20,min = 3,message = "Size bwt 3-20")
    private String username;
    @NotBlank
    @Size(max = 30)
    private String password;
    @Email
    @Size(max = 50)
    private String email;
    private String role;
}
