package com.apgroup.gateway;

import com.apgroup.common.KeyLoader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

@Component
public class JwtAuthFilter implements WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    private static final Key SECRET_KEY = KeyLoader.loadKeyFromEnv();

    private ServerWebExchange giveExchangeCors(ServerWebExchange exchange) {
        exchange.getResponse().getHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponse().getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponse().getHeaders().add("Access-Control-Allow-Headers", "Authorization, Content-Type");
        exchange.getResponse().getHeaders().add("Access-Control-Max-Age", "3600");
        return exchange;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequest().getMethod().name())) {
            logger.info("Allowing OPTIONS request");

            exchange.getResponse().setStatusCode(HttpStatus.OK);
            exchange = giveExchangeCors(exchange);
            return exchange.getResponse().setComplete();
        }

        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/auth/")) {
            logger.info("Allowing unauthenticated request to path: {}", path);
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Missing or invalid Authorization header");
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            exchange = giveExchangeCors(exchange);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            logger.info("Authenticated request for user: {}", claims.getSubject());

            return chain.filter(exchange);
        } catch (JwtException e) {
            logger.error("JWT validation failed: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            exchange = giveExchangeCors(exchange);
            return exchange.getResponse().setComplete();
        }
    }
}
