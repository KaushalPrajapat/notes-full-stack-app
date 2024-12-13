package com.notes.notes_app.services.Impl;

import com.notes.notes_app.dtos.UserDTOResponse;
import com.notes.notes_app.exceptions.CustomException;
import com.notes.notes_app.models.*;
import com.notes.notes_app.repos.PasswordResetTokenRepository;
import com.notes.notes_app.repos.RefreshTokenRepository;
import com.notes.notes_app.repos.RoleRepository;
import com.notes.notes_app.repos.UserRepository;
import com.notes.notes_app.security.jwt.JwtUtils;
import com.notes.notes_app.security.request.LoginRequest;
import com.notes.notes_app.security.request.RefreshTokenRequest;
import com.notes.notes_app.security.request.SignupRequest;
import com.notes.notes_app.security.response.LoginResponse;
import com.notes.notes_app.security.response.MessageResponse;
import com.notes.notes_app.services.AuthService;
import com.notes.notes_app.security.service.UserDetailsImpl;
import com.notes.notes_app.services.UserLogsService;
import com.notes.notes_app.services.UserService;
import com.notes.notes_app.utils.EmailUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@PropertySource("classpath:props.properties")
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserLogsService userLogsService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private EmailUtils emailUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${frontend.ip.url}")   // need to change it based on network to access app on phone
    private String ipUrl;

    @Override
    public LoginResponse signin(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            throw new CustomException("EXCEPTION!!!! Bad credentials Authentication failed", "BAD_CREDENTIALS");
        }
        //Set the authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.findUserById(userDetails.getId());
        String accessToken;
        String refreshToken;
        if (refreshTokenRepository.existsByUser(user)) {
            accessToken = jwtUtils.generateAccessToken(userDetails);
            RefreshToken token = refreshTokenRepository.findByUser(user);
            refreshToken = token.getRefreshToken();
            token.setLastAccessToken(accessToken);
            refreshTokenRepository.save(token);
        } else {
            accessToken = jwtUtils.generateAccessToken(userDetails);
            refreshToken = jwtUtils.generateRefreshTokenToken(userDetails);
            RefreshToken refreshTokenEntity = new RefreshToken();
            refreshTokenEntity.setRefreshToken(refreshToken);
            refreshTokenEntity.setLastAccessToken(accessToken);
            refreshTokenEntity.setUser(user);
            refreshTokenRepository.save(refreshTokenEntity);
        }

        // Collect roles from the UserDetails
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // Prepare the response body, now including the JWT token directly in the body
        // Return the response entity with the JWT token included in the response body
        return LoginResponse.builder().
                accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .roles(roles)
                .httpStatus(200)
                .build();
    }

    @Override
    public UserDTOResponse addUser(SignupRequest signUpRequest) {
        if (signUpRequest.getUsername().isEmpty()) {
            signUpRequest.setUsername(signUpRequest.getEmail().split("@")[0]);
        }
        // If user Already exists throw error - make your own custom error
        if (userRepository.existsByEmailOrUsername(signUpRequest.getEmail(), signUpRequest.getUsername())) {
            throw new CustomException("Email or UserName Already Exists !!", "USER_EXISTS");
        }
        //  Prepare User
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), passwordEncoder.encode(signUpRequest.getPassword()));
        user.setSignUpMethod("email");
        if (signUpRequest.getRole().isEmpty()
                || signUpRequest.getRole().toUpperCase().contains("GUEST")) {
            user.setSignUpMethod("guest_email");
        }
        user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
        user.setAccountExpiryDate(LocalDate.now().plusYears(2));
        if (signUpRequest.getRole().toUpperCase().contains("USER") && roleRepository.existsByRoleName(AppRole.ROLE_USER)) {
            user.setRole(roleRepository.findByRoleName(AppRole.ROLE_USER).orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER))));
        } else if (signUpRequest.getRole().toUpperCase().contains("ADMIN") && roleRepository.existsByRoleName(AppRole.ROLE_ADMIN)) {
            user.setRole(roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN))));
        } else {
            user.setCredentialsExpiryDate(LocalDate.now().plusDays(30));
            user.setAccountExpiryDate(LocalDate.now().plusDays(60));
            user.setRole(roleRepository.findByRoleName(AppRole.ROLE_GUEST).orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_GUEST))));
        }
        //Save user
        User savedUser = userRepository.save(user);

        UserDTOResponse userDTOResponse = new UserDTOResponse();
        // If user is trying to create account He needs to validate account so sending email
        String rolesString = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toList().toString();
        if (!rolesString.toUpperCase().contains("ADMIN")
                && !rolesString.toUpperCase().contains("SU")
                && !signUpRequest.getRole().toUpperCase().contains("GUEST")) {
            // If  user is creating then validation need to be done, So setting enabled and accountNonLock status as false
            user.setEnabled(false);
            user.setAccountNonLocked(false);
            userRepository.save(user);
            //Prepare Email
            String token = UUID.randomUUID().toString();
            Instant expirationTime = Instant.now().plus(24, ChronoUnit.HOURS);
            PasswordResetToken resetProps = new PasswordResetToken(token, expirationTime, savedUser);
            passwordResetTokenRepository.save(resetProps);
//            yet not implemented in frontend
            //In the future, I need to attach frontend or backend link here like http://localhost:8080
            String resetLink = "http://localhost:8080/api/auth/public/validate-user?token=" + token;
            // DO EMAIL SERVICE
            emailUtils.sendBasicEmail(signUpRequest.getEmail(), resetLink);
            System.out.println("mail sent " + signUpRequest.getEmail());
            userDTOResponse.setMessage("Validate Your Account by clicking on mail received on your email-id");
        }
        if (signUpRequest.getRole().toUpperCase().contains("GUEST")) {
            userDTOResponse.setMessage("You are a Guest User!! Please create permanent account!! to save and access you notes");
        }

        //Prepare and Response for Admin (If he's creating account)
        BeanUtils.copyProperties(savedUser, userDTOResponse);
        userDTOResponse.setCreatedDate(user.getCreatedDate().toString().substring(0, 10) + " at " + user.getCreatedDate().toString().substring(11, 19));
        userDTOResponse.setUpdatedDate(user.getUpdatedDate().toString().substring(0, 10) + " at " + user.getUpdatedDate().toString().substring(11, 19));

        //Prepare User change log to be added
        Map<String, String> changes = new HashMap<>();
        changes.put("isNEW_USER_CREATED", "CREATED");
        userLogsService.createAChangeLogAndSave(savedUser.getUserId(), changes);

        return userDTOResponse;
    }

    @Override
    public UserDTOResponse getUserDetails(UserDetailsImpl userDetails) {
        User user = userService.findByUserName(userDetails.getUsername());
        //List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        UserDTOResponse response = new UserDTOResponse();
        if (user.getRole().getRoleName().equals(AppRole.ROLE_GUEST)) {
            response.setMessage("You are a Guest User!! Please create permanent account!! to save and access you notes");
        }
        BeanUtils.copyProperties(user, response);
        return response;
    }

    @Override
    public MessageResponse generatePasswordResetToken(String email) {
        User user = userService.findUserByEmail(email).orElseThrow(() ->
                new CustomException("User with given email doesn't exists", "USER_NOT_FOUND"));
        String token = UUID.randomUUID().toString();
        Instant expirationTime = Instant.now().plus(24, ChronoUnit.HOURS);
        PasswordResetToken resetProps = new PasswordResetToken(token, expirationTime, user);
        passwordResetTokenRepository.save(resetProps);
        //In the future, I need to attach frontend or backend link here like http://localhost:8080
        String resetLink = ipUrl+ "/reset-password?token=" + token;
        // DO EMAIL SERVICE
        emailUtils.sendBasicEmail(email, resetLink);
        return new MessageResponse("Password reset link sent successfully", 200);
    }

    @Override
    public MessageResponse resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(
                () -> new CustomException("Password reset Token is not valid.", "INVALID_TOKEN")
        );
        if (resetToken.isUsed()) {
            throw new CustomException("Token is already used.", "INVALID_TOKEN");
        }
        if (resetToken.getExpirationTime().isBefore(Instant.now())) {
            throw new CustomException("Token is expired.", "INVALID_TOKEN");
        }
        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
        User user = resetToken.getForUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new CustomException("Some issue at database", "CAN'T_CHANGE_PASSWORD");
        }
        return new MessageResponse("Password changed Successfully", 200);
    }

    //  After creating account, by user throw public api he needs to validate his account
    @Override
    public MessageResponse validateUser(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(
                () -> new CustomException("User validation Token is not valid.", "INVALID_TOKEN")
        );
        if (resetToken.isUsed()) {
            throw new CustomException("Token is already used.", "INVALID_TOKEN");
        }
        if (resetToken.getExpirationTime().isBefore(Instant.now())) {
            throw new CustomException("Token is expired.", "INVALID_TOKEN");
        }
        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
        User user = resetToken.getForUser();
        user.setEnabled(true);
        user.setAccountNonLocked(true);

        Map<String, String> changes = new HashMap<>();
        changes.put("isUserValidated", "true");
        userLogsService.createAChangeLogAndSave(user.getUserId(), changes);

        userRepository.save(user);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new CustomException("Some issue at database", "CAN'T_VALIDATE_USER");
        }
        return new MessageResponse("User  Verified SuccessFully!! 😁😁 Enjoy", 200);
    }

    @Override
    public LoginResponse getMeRefreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refToken = refreshTokenRequest.getRefreshToken();
        RefreshToken token = refreshTokenRepository.findByRefreshToken(refToken).orElseThrow(() ->
                new CustomException("Invalid refresh token login again", "INVALID_REFRESH_TOKEN"));

        //Generate new access Token
        User user = token.getUser();
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleName().name());
        //Set the authentication
        UserDetailsImpl userDetails = new UserDetailsImpl(user.getUserId(), user.getUsername(), user.getEmail(), "",
                user.isTwoFactorEnabled(), user.isAccountNonLocked(), user.isAccountNonExpired(),
                user.isCredentialsNonExpired(), user.isEnabled(), user.isShouldResetToken(),
                List.of(authority));
        String accessToken;
        String refreshToken;
        accessToken = jwtUtils.generateAccessToken(userDetails);
        refreshToken = token.getRefreshToken();
        token.setLastAccessToken(accessToken);
        refreshTokenRepository.save(token);

        // Collect roles from the UserDetails
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // Prepare the response body, now including the JWT token directly in the body
        // Return the response entity with the JWT token included in the response body
        return LoginResponse.builder().
                accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .roles(roles).
                httpStatus(200)
                .build();
    }
}
