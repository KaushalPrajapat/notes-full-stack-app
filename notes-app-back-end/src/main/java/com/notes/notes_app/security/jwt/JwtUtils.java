package com.notes.notes_app.security.jwt;

import com.notes.notes_app.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@PropertySource("classpath:props.properties")
@Component
public class JwtUtils {
    private static Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtAccessExpirationMs}")
    private long jwtAccessExpirationMs;


    @Value("${spring.app.jwtRefreshExpirationMs}")
    private long jwtRefreshExpirationMs;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateAccessToken(UserDetailsImpl userDetails) {
        return generateTokenFromUserName(userDetails, jwtAccessExpirationMs);
    }

    public String generateRefreshTokenToken(UserDetailsImpl userDetails) {
        return generateTokenFromUserName(userDetails, jwtRefreshExpirationMs);
    }

    private String generateTokenFromUserName(UserDetailsImpl userDetails, long expirationTime) {
        String username = userDetails.getUsername();
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        JwtBuilder builder = Jwts.builder();
        builder.subject(username);
        builder.claim("roles", roles);
        builder.claim("shouldResetToken", !userDetails.isShouldResetToken());
        builder.claim("is2FaEnabled", userDetails.is2faEnabled());
        builder.issuedAt(new Date());
        builder.expiration(new Date(new Date().getTime() + expirationTime));
        builder.signWith(key());
        return builder.compact();
    }


    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String getUserNameFromJwtToken(String token) {
        if (extractAllClaims(token) == null) return null;
        else return Objects.requireNonNull(extractAllClaims(token)).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private boolean extractShouldResetToken(String token) {
        return extractAllClaims(token).get("shouldResetToken", Boolean.class);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().verifyWith((SecretKey) key())
                    .build().parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    public String extractJwtTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Bearer Token : {}", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateJwtToken(UserDetailsImpl userDetails, String authToken) {
        try {
            boolean isTokenExpired = isTokenExpired(authToken);
            String getUserNameFromJwtToken = getUserNameFromJwtToken(authToken);
            return !isTokenExpired && userDetails.getUsername().equalsIgnoreCase(getUserNameFromJwtToken) && extractShouldResetToken(authToken) != userDetails.isShouldResetToken();
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
