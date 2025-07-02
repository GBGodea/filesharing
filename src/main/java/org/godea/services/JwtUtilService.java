package org.godea.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.godea.di.Service;
import org.godea.models.User;
import org.yaml.snakeyaml.Yaml;

import javax.crypto.SecretKey;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class JwtUtilService {
    private final String KEY;

    public JwtUtilService() {
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

        KEY = ymlSource.get("token");
    }

    public String generateToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        Instant expirationInstant = now.plusDays(7).atZone(ZoneId.systemDefault()).toInstant();
        Date expiration = Date.from(expirationInstant);

        return Jwts.builder()
                .subject(user.getEmail())
                .expiration(expiration)
                .signWith(getSecretKey())
                .claim("role", user.getRole().getRole().name())
                .compact();
    }

    public boolean validate(String token) {
        try {
            parse(token);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        byte[] encode = Base64.getDecoder().decode(KEY);
        return Keys.hmacShaKeyFor(encode);
    }
}
