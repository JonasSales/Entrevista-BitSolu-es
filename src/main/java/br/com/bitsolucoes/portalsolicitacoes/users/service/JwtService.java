package br.com.bitsolucoes.portalsolicitacoes.users.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final Key signingKey;
    private final long expirationMillis;

    public JwtService(@Value("${security.jwt.secret}") String secret,
                      @Value("${security.jwt.expiration-minutes}") long expirationMinutes) {
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("JWT_SECRET deve possuir pelo menos 32 caracteres.");
        }
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = expirationMinutes * 60_000L;
    }

    public String generateToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(signingKey)
                .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isValid(String token, String username) {
        Claims claims = parseClaims(token);
        return claims.getSubject().equals(username) && claims.getExpiration().after(new Date());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith((javax.crypto.SecretKey) signingKey).build()
                .parseSignedClaims(token).getPayload();
    }
}
