
package tripora.api.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tripora.api.domain.User;
import tripora.api.dto.AuthResponse;
import tripora.api.dto.LoginRequest;
import tripora.api.dto.RegisterRequest;
import tripora.api.dto.UpdateProfileRequest;
import tripora.api.service.auth.AuthService;

@RestController
@RequestMapping("/v1/api/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        try {
            log.info("Register request received for email: {}", req.email());
            AuthResponse response = authService.register(req);


            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error during registration for email {}: {}", req.email(), e.getMessage(), e);
            // Return generic message for security reasons
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed. Please check your input or try again later.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        try {
            AuthResponse response = authService.login(req);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login failed for email {}: {}", req.email(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials.");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        try {
            String email = auth.getName();
            User user = authService.getMe(email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Failed to get current user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not fetch user data.");
        }
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMe(Authentication auth,
                                      @Valid @RequestBody UpdateProfileRequest req) {
        try {
            String email = auth.getName();
            User updatedUser = authService.updateProfile(email, req);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            log.error("Failed to update profile for user {}: {}", auth.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Profile update failed. Please check your input.");
        }
    }
}