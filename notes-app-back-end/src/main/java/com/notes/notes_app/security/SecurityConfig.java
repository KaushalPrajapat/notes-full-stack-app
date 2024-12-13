package com.notes.notes_app.security;


import com.notes.notes_app.config.OAuth2LoginSuccessHandler;
import com.notes.notes_app.models.AppRole;
import com.notes.notes_app.models.Role;
import com.notes.notes_app.models.User;
import com.notes.notes_app.repos.RoleRepository;
import com.notes.notes_app.repos.UserRepository;
import com.notes.notes_app.security.jwt.AuthTokenFilter;
import com.notes.notes_app.security.jwt.JwtAuthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@PropertySource("classpath:props.properties")
@Configuration
@EnableWebSecurity
@CrossOrigin
@EnableMethodSecurity(prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig {
    @Value("${frontend.react.url}")
    private String reactUrl;
    @Value("${frontend.vite.url}")
    private String viteUrl;
    @Value("${frontend.ip.url}")   // need to change it based on network to access app on phone
    private String ipUrl;


    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    @Autowired
    @Lazy
    OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//        http.csrf(csrf ->
//                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .ignoringRequestMatchers("/api/auth/public/**")
//        );
        http
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(List.of(reactUrl, viteUrl, ipUrl)); // React frontend URL
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }));
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests((requests)
                        -> requests
                        .requestMatchers("/api/auth/public/**", "/v3/api-docs", "/swagger-ui/**").permitAll() //signin-signup
                        .requestMatchers("/api/csrf-token").permitAll()
                        .requestMatchers("/api/admin/**").hasAnyRole("ADMIN", "SU")
                        .requestMatchers("/api/user/**").hasAnyRole("USER")
                        .requestMatchers("/api/guest/**").hasAnyRole("GUEST")
                        .requestMatchers("/api/su/**").hasRole("SU")
                        .requestMatchers("/api/note/**").hasAnyRole("ADMIN", "SU", "USER", "GUEST")
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/api/notes/log/**").hasAnyRole("ADMIN", "SU", "USER", "GUEST")     // But kewal apana dekh sakate h bhai, Jis note k tum owner ho
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> {
                    oauth2.successHandler(oAuth2LoginSuccessHandler);
                });

        http.exceptionHandling(exception
                -> exception.authenticationEntryPoint(unauthorizedHandler));
        http.addFilterBefore(authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            Role suRole = roleRepository.findByRoleName(AppRole.ROLE_SU)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_SU)));
            if (!userRepository.existsByUsername("su")) {
                User superUser = new User("su", "su@example.com",
                        passwordEncoder.encode("su"));
                superUser.setAccountNonLocked(true);
                superUser.setAccountNonExpired(true);
                superUser.setCredentialsNonExpired(true);
                superUser.setEnabled(true);
                superUser.setCredentialsExpiryDate(LocalDate.now().plusYears(100));
                superUser.setAccountExpiryDate(LocalDate.now().plusYears(100));
                superUser.setTwoFactorEnabled(false);
                superUser.setSignUpMethod("superUser");
                superUser.setRole(suRole);
                userRepository.save(superUser);
            }

            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));
            if (!userRepository.existsByUsername("user")) {
                User user1 = new User("user", "user@example.com",
                        passwordEncoder.encode("user"));
                user1.setAccountNonLocked(false);
                user1.setAccountNonExpired(true);
                user1.setCredentialsNonExpired(true);
                user1.setEnabled(true);
                user1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                user1.setAccountExpiryDate(LocalDate.now().plusYears(1));
                user1.setTwoFactorEnabled(false);
                user1.setSignUpMethod("email");
                user1.setRole(userRole);
                userRepository.save(user1);
            }

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User("admin", "admin@example.com",
                        passwordEncoder.encode("admin"));
                admin.setAccountNonLocked(true);
                admin.setAccountNonExpired(true);
                admin.setCredentialsNonExpired(true);
                admin.setEnabled(true);
                admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
                admin.setTwoFactorEnabled(false);
                admin.setSignUpMethod("email");
                admin.setRole(adminRole);
                userRepository.save(admin);
            }

            Role guestRole = roleRepository.findByRoleName(AppRole.ROLE_GUEST)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_GUEST)));
            if (!userRepository.existsByUsername("guest")) {
                User guest = new User("guest", "guest@example.com",
                        passwordEncoder.encode("guest"));
                guest.setAccountNonLocked(true);
                guest.setAccountNonExpired(true);
                guest.setCredentialsNonExpired(true);
                guest.setEnabled(true);
                guest.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                guest.setAccountExpiryDate(LocalDate.now().plusYears(1));
                guest.setTwoFactorEnabled(false);
                guest.setSignUpMethod("email");
                guest.setRole(guestRole);
                userRepository.save(guest);
            }

            if (!userRepository.existsByUsername("self")) {
                User self = new User("self", "self@example.com",
                        passwordEncoder.encode("self"));
                self.setAccountNonLocked(true);
                self.setAccountNonExpired(true);
                self.setCredentialsNonExpired(true);
                self.setEnabled(true);
                self.setCredentialsExpiryDate(LocalDate.now().plusYears(5));
                self.setAccountExpiryDate(LocalDate.now().plusYears(5));
                self.setTwoFactorEnabled(false);
                self.setSignUpMethod("email");
                self.setRole(suRole);
                userRepository.save(self);
            }
        };
    }
}