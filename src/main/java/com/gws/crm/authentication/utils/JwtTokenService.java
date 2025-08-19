package com.gws.crm.authentication.utils;

import com.gws.crm.authentication.dto.UserDetailsDTO;
import com.gws.crm.authentication.entity.Role;
import com.gws.crm.authentication.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Slf4j
@Service
public class JwtTokenService {

    private static final Set<String> ALLOWED_ROLES = Set.of("USER", "ADMIN", "SUPER_ADMIN");
    @Value("${security.jwt.secret.key}")
    private String jwtSecretKey;
    @Value("${security.jwt.refresh.expiration}")
    private int refreshExpirationTime;
    @Value("${security.jwt.access.expiration}")
    private int accessExpirationTime;

    public String generateRefreshToken(User user) {
        return generateToken(user, generateExtraClaims(user), refreshExpirationTime);
    }

    public String generateAccessToken(User user) {
        return generateToken(user, generateExtraClaims(user), accessExpirationTime);
    }

    public String generateRefreshToken(UserDetailsDTO user) {
        return generateToken(user, generateExtraClaims(user), refreshExpirationTime);
    }

    public String generateAccessToken(UserDetailsDTO user) {
        return generateToken(user, generateExtraClaims(user), accessExpirationTime);
    }

    public long extractUserId(String token) {
        try {
            String userIdStr = extractClaim(token, Claims::getId);
            return Long.parseLong(userIdStr);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtValidationException("Failed to extract user ID from token.",
                    List.of(new OAuth2Error("invalid_token", "Token is invalid or malformed.", null)));
        }
    }

    public String extractUserName(String token) {
        try {
            return extractClaim(token, Claims::getIssuer);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtValidationException("Failed to extract user Name from token.",
                    List.of(new OAuth2Error("invalid_token", "Token is invalid or malformed.", null)));
        }
    }

    private Map<String, Object> generateExtraClaims(User user) {
        String roleName = user.getRoles().stream()
                .map(Role::getName)
                .filter(ALLOWED_ROLES::contains)
                .findFirst()
                .orElse("USER");

        return Map.of("authority", user.getMainRoles(), "role", roleName);
    }

    private Map<String, Object> generateExtraClaims(UserDetailsDTO user) {
        String roleName = user.getRoles().stream()
                .map(Role::getName)
                .filter(ALLOWED_ROLES::contains)
                .findFirst()
                .orElse("USER");

        return Map.of("authority", user.getMainRoles(), "role", roleName);
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtException("Token has expired", e);
        } catch (UnsupportedJwtException e) {
            throw new JwtException("Unsupported JWT token", e);
        } catch (MalformedJwtException e) {
            throw new JwtException("Malformed JWT token", e);
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature", e);
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT claims string is empty", e);
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken(User user, Map<String, Object> extraClaims, int expirationTime) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setId(String.valueOf(user.getId()))
                .setIssuer(user.getName())
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateToken(UserDetailsDTO user, Map<String, Object> extraClaims, int expirationTime) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setId(String.valueOf(user.getId()))
                .setIssuer(user.getName())
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expirationDate = extractClaim(token, Claims::getExpiration);
            Date now = new Date();

            log.info("Checking token expiration:");
            log.info("  Expiration time: {}", expirationDate);
            log.info("  Current time: {}", now);
            log.info("  Is expired: {}", expirationDate.before(now));

            return expirationDate.before(now);
        } catch (Exception e) {
            log.error("Error checking token expiration: {}", e.getMessage());
            return true; // في حالة خطأ، اعتبر الـ token expired
        }
    }

    public String extractUserRole(String token) {
        try {
            return extractClaim(token, claims -> (String) claims.get("role"));
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtValidationException("Failed to extract role from token.",
                    List.of(new OAuth2Error("invalid_token", "Token is invalid or malformed.", null)));
        }
    }
}
