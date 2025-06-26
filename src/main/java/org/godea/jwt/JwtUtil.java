package org.godea.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.godea.models.User;
import org.yaml.snakeyaml.Yaml;

import javax.crypto.SecretKey;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private final SecretKey KEY;

    public JwtUtil() {
        InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("application.yml");

        System.out.println(inputStream);

        if (inputStream == null) {
            throw new RuntimeException("Input stream is empty");
        }

        Yaml yml = new Yaml();
        Map<String, Object> data = yml.load(inputStream);
        @SuppressWarnings("unchecked")
        Map<String, String> ymlSource = (Map<String, String>) data.get("jwt");

        KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(ymlSource.get("token")));
    }

    public String generateToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        Instant expirationInstant = now.plusDays(7).atZone(ZoneId.systemDefault()).toInstant();
        Date expiration = Date.from(expirationInstant);

        return Jwts.builder()
                .subject(user.getEmail())
                .expiration(expiration)
                .signWith(KEY)
                .claim("role", user.getRole().getRole().name())
                .compact();
    }

    private boolean validateToken(String token, SecretKey secret) {
        try {
            Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token);
        }
    }
}
