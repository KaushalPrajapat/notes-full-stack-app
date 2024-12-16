package com.notes.notes_app.config;

import com.notes.notes_app.models.AppRole;
import com.notes.notes_app.models.RefreshToken;
import com.notes.notes_app.models.Role;
import com.notes.notes_app.models.User;
import com.notes.notes_app.repos.RefreshTokenRepository;
import com.notes.notes_app.repos.RoleRepository;
import com.notes.notes_app.security.jwt.JwtUtils;
import com.notes.notes_app.security.service.UserDetailsImpl;
import com.notes.notes_app.services.UserLogsService;
import com.notes.notes_app.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@PropertySource("classpath:props.properties")
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserLogsService userLogsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${frontend.react.url}")
    private String frontendUrl;

    String username;
    String idAttributeKey;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        if ("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()) || "google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            String email = attributes.getOrDefault("email", "").toString();
            String name = attributes.getOrDefault("name", "").toString();
            if ("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
                username = attributes.getOrDefault("login", "").toString();
                idAttributeKey = "id";
            } else if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
                username = email.split("@")[0];
                idAttributeKey = "sub";
            } else {
                username = "";
                idAttributeKey = "id";
            }
//            System.out.println("HELLO OAUTH: " + email + " : " + name + " : " + username);

            userService.findUserByEmail(email)
                    .ifPresentOrElse(user -> {
                        DefaultOAuth2User oauthUser = new DefaultOAuth2User(
                                List.of(new SimpleGrantedAuthority(user.getRole().getRoleName().name())),
                                attributes,
                                idAttributeKey
                        );
                        Authentication securityAuth = new OAuth2AuthenticationToken(
                                oauthUser,
                                List.of(new SimpleGrantedAuthority(user.getRole().getRoleName().name())),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
                        );
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    }, () -> {
                        User newUser = new User();
                        Optional<Role> userRole = roleRepository.findByRoleName(AppRole.ROLE_USER); // Fetch existing role
                        if (userRole.isPresent()) {
                            newUser.setRole(userRole.get()); // Set existing role
                        } else {
                            // Handle the case where the role is not found
                            newUser.setRole(new Role(AppRole.ROLE_GUEST));
                            throw new RuntimeException("Default role not found");
                        }
                        newUser.setEmail(email);
                        newUser.setUsername(username);
                        newUser.setSignUpMethod(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                        newUser.setEnabled(true);
                        newUser.setShouldResetToken(true);
                        newUser.setCredentialsNonExpired(true);
                        newUser.setAccountNonLocked(true);
                        newUser.setAccountExpiryDate(LocalDate.now().plusYears(1));
                        newUser.setCredentialsExpiryDate(LocalDate.now().plusYears(2));
                        var savedUser = userService.registerUser(newUser);
                        //Prepare User change log to be added
                        Map<String, String> changes = new HashMap<>();
                        changes.put("isNEW_USER_CREATED", "CREATED");
                        changes.put("oauth2", "oauth2");
                        userLogsService.createAChangeLogAndSave(savedUser.getUserId(), changes);
                        DefaultOAuth2User oauthUser = new DefaultOAuth2User(
                                List.of(new SimpleGrantedAuthority(newUser.getRole().getRoleName().name())),
                                attributes,
                                idAttributeKey
                        );
                        Authentication securityAuth = new OAuth2AuthenticationToken(
                                oauthUser,
                                List.of(new SimpleGrantedAuthority(newUser.getRole().getRoleName().name())),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
                        );
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    });
        }
        this.setAlwaysUseDefaultTargetUrl(true);

        // JWT TOKEN LOGIC
        DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauth2User.getAttributes();

        // Extract necessary attributes
        String email = (String) attributes.get("email");
//        System.out.println("OAuth2LoginSuccessHandler: " + username + " : " + email);

        Set<SimpleGrantedAuthority> authorities = oauth2User.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority())).collect(Collectors.toSet());
        User user = userService.findUserByEmail(email).orElseThrow(
                () -> new RuntimeException("User not found"));
        authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName().name()));

        // Create UserDetailsImpl instance
        UserDetailsImpl userDetails = new UserDetailsImpl(
                null,
                username, //username
                email, //email
                null,
                false,
                true,
                true,
                true,
                true,
                true,
                authorities
        );
        // Generate JWT token
        String accessToken = jwtUtils.generateAccessToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshTokenToken(userDetails);
//        Save refresh token
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

//        Saved refresh token for future
        // Redirect to the frontend with the JWT token
        String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/redirect")
                .queryParam("token", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();
//        System.out.println(targetUrl);
        this.setDefaultTargetUrl(targetUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}