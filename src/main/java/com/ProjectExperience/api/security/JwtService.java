package com.ProjectExperience.api.security;


import com.ProjectExperience.api.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ==========================
    // GERAR TOKEN
    // ==========================

    public String generateToken(AuthenticatedUser user) {

        Date now = new Date();

        Date expiration = new Date(
                now.getTime() + jwtProperties.getExpirationMs()
        );

        return Jwts.builder()
                .subject(user.getUsername()) // email
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    // ==========================
    // EXTRAIR EMAIL
    // ==========================

    public String extractUsername(String token) {

        return getClaims(token).getSubject();
    }

    // ==========================
    // VALIDAR TOKEN
    // ==========================

    public boolean isTokenValid(String token,
                                AuthenticatedUser user) {

        String username = extractUsername(token);

        return username.equals(user.getUsername())
                && !isTokenExpired(token);
    }

    // ==========================
    // TOKEN EXPIRADO?
    // ==========================

    private boolean isTokenExpired(String token) {

        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // ==========================
    // CLAIMS
    // ==========================

    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}