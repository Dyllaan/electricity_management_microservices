package com.apgroup.auth.controller;

import com.apgroup.auth.dto.AuthRequest;
import com.apgroup.auth.http.ResponseFactory;
import com.apgroup.auth.model.Token;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;

/**
 * @author Louis Figes (W21017657)
 * This class is responsible for handling user authentication requests.
 * only login and register are allowed
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final InMemoryUserDetailsManager userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(AuthenticationManager authenticationManager, InMemoryUserDetailsManager userDetailsService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest loginRequest) {
        logger.info("Login request received for user: {}", loginRequest.username());
        try {
            logger.info("Authenticating user: {}", loginRequest.username());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );

            logger.info("User authenticated: {}", loginRequest.username());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.info("Generating token for user: {}", loginRequest.username());

            Token token = Token.generate(loginRequest.username());
            return ResponseEntity.ok(ResponseFactory.success(loginRequest.username(), token.getToken()));
        } catch (AuthenticationException e) {
            logger.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(401).body(ResponseFactory.error("Invalid username or password"));
        } catch (Exception e) {
            logger.error("Internal server error: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(ResponseFactory.error("Internal server error"));
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest registerRequest) {
        try {
            if (userDetailsService.userExists(registerRequest.username())) {
                return ResponseEntity.status(400).body(ResponseFactory.error("User already exists"));
            }

            UserDetails user = User.builder()
                    .username(registerRequest.username())
                    .password(passwordEncoder.encode(registerRequest.password()))
                    .roles("USER")
                    .build();
            userDetailsService.createUser(user);
            Token token = Token.generate(registerRequest.username());
            return ResponseEntity.ok(ResponseFactory.success(registerRequest.username(), token.getToken()));
        } catch (Exception e) {
            logger.error("Internal server error: {}", e.getMessage());
            return ResponseEntity.status(500).body(ResponseFactory.error("Internal server error"));
        }
    }

    /**
     * Dont need to check token validity or respond appropriately itll do it for us
     */
    @GetMapping
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(ResponseFactory.currentUser(userDetails.getUsername()));
    }

}
