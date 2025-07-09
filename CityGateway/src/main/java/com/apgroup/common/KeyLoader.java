package com.apgroup.common;

import io.github.cdimascio.dotenv.Dotenv;

import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.Base64;

/**
 * @author Louis Figes (W21017657)
 */
public class KeyLoader {

    private static final Logger logger = LoggerFactory.getLogger(KeyLoader.class);

    private static final Dotenv env = Dotenv.load();

    /**
     * Loads the secret key from the environment
     * @return the secret key
     */
    public static Key loadKeyFromEnv() {
        try {
            String secret = env.get("SECRET_KEY");
            if (secret == null || secret.isEmpty()) {
                throw new IllegalStateException("SECRET_KEY is not set in the .env file");
            }
            byte[] decodedKey = Base64.getDecoder().decode(secret);
            return Keys.hmacShaKeyFor(decodedKey);
        } catch (Exception e) {
            logger.error("Error loading secret key from env: {}", e.getMessage(), e);
            throw new IllegalStateException("Error loading secret key from env", e);
        }
    }
}
