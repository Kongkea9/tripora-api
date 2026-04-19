
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
import tripora.api.security.UserPrincipal;
import tripora.api.service.auth.AuthServiceImpl;

@RestController
@RequestMapping("/v1/api/auth")
@Slf4j
public class AuthController {

    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        log.info("Register request received for email: {}", req.email());
        AuthResponse response = authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
            UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

            User user = authService.getMe(principal.email());

            return ResponseEntity.ok(user);

        } catch (Exception e) {
            log.error("Failed to get current user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not fetch user data.");
        }
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateMe(Authentication auth,
                                         @Valid @RequestBody UpdateProfileRequest req) {

        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        String email = principal.email();
        log.info("Authenticated user: {}", auth.getName());
        User updatedUser = authService.updateProfile(email, req);
        return ResponseEntity.ok(updatedUser);
    }
}