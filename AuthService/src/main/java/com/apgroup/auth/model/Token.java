package com.apgroup.auth.model;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.Date;

/**
 * OO Implementation of JWS token
 * JWS is JWT But signed
 * based on the JJWT docs: https://github.com/jwtk/jjwt?tab=readme-ov-file#signed-jwts
 * and https://www.baeldung.com/spring-security-sign-jwt-token
 * essentially if we sign the token we can verify its authenticity (is from our API)
 * @author Louis Figes (W21017657)
 */
public class Token {

    private static Logger logger = LoggerFactory.getLogger(Token.class);

    private static final int EXP_TIME = 1000 * 60 * 60 * 24; // 24 hours
    private static final Key SECRET_KEY = generateKey();

    private final String token;
    private final Claims claims;

    public Token(String token) {
        this.token = token;
        this.claims = parse(token);
    }

    /**
     * Generates a secure HMAC key at startup.
     */
    private static Key generateKey() {
        try {
            logger.info("Generating secure HMAC key");
            return Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } catch (Exception e) {
            logger.error("Error generating SECRET_KEY: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to generate signing key", e);
        }
    }


    /**
     * Token is valid for 24 hours
     * @param username username to generate token for
     * @return
     */
    public static Token generate(String username) {
        try {
            String jwt = Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXP_TIME))
                    .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                    .compact();
            return new Token(jwt);
        } catch (Exception e) {
            logger.error("Error generating token: {}", e.getMessage());
            throw new RuntimeException("Error generating token");
        }
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return claims.getSubject();
    }

    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    public boolean validate(String username) {
        try {
            return username.equals(getUsername()) && !isExpired();
        } catch (Exception e) {
            logger.error("Error validating token: {}", e.getMessage());
            return false;
        }
    }

    private Claims parse(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            logger.error("Error parsing token: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token");
        }
    }
}
