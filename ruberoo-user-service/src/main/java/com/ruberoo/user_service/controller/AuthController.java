package com.ruberoo.user_service.controller;

import com.ruberoo.user_service.dto.LoginRequest;
import com.ruberoo.user_service.entity.User;
import com.ruberoo.user_service.jwt.JwtTokenGenerator;
import com.ruberoo.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/users/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final PasswordEncoder passwordEncoder; // Used for secure password checking

    public AuthController(UserRepository userRepository,
                          JwtTokenGenerator jwtTokenGenerator,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Handles the user login request and generates a JWT token upon successful authentication.
     * Endpoint: /api/users/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // 1. Find user by email
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());

        if (userOpt.isEmpty()) {
            // Return 401 Unauthorized for security (do not confirm if user exists)
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid credentials."));
        }

        User user = userOpt.get();

        // 2. Verify password
        // Log length and match result for debugging
        String storedHash = user.getPassword();
        boolean matches = false;
        try {
            matches = passwordEncoder.matches(loginRequest.getPassword(), storedHash);
        } catch (Exception ex) {
            logger.error("Error while matching password for user {}: {}", user.getEmail(), ex.getMessage());
        }
        logger.info("Auth attempt for email={} storedHashLength={} matches={}", user.getEmail(), (storedHash == null ? 0 : storedHash.length()), matches);

        if (!matches) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Invalid credentials."));
        }

        // 3. Generate JWT token
        String jwt = jwtTokenGenerator.generateToken(user.getEmail(), user.getId());

        // 4. Return the JWT to the client
        return ResponseEntity.ok(Collections.singletonMap("token", jwt));
    }
}
