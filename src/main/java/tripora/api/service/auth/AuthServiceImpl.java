package tripora.api.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tripora.api.config.JwtService;
import tripora.api.domain.Role;
import tripora.api.domain.User;
import tripora.api.dto.AuthResponse;
import tripora.api.dto.LoginRequest;
import tripora.api.dto.RegisterRequest;
import tripora.api.dto.UpdateProfileRequest;
import tripora.api.repository.RoleRepository;
import tripora.api.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;




@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;


    public AuthResponse register(RegisterRequest req) {
        if(userRepository.existsByEmail(req.email())) {
            throw new RuntimeException("Email already exists");
        }

        Optional<User> existingUser = userRepository.findUserByEmail(req.email());
        if(existingUser.isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("USER");
                    return roleRepository.save(newRole);
                });

        User user = new User();
        user.setName(req.name());
        user.setEmail(req.email());
        user.setPasswordHash(encoder.encode(req.password()));
        user.setRoles(Set.of(userRole));
        user.setCreateAt(LocalDate.now());

        userRepository.save(user);

        return new AuthResponse(jwtService.generateToken(user));
    }


    public AuthResponse login(LoginRequest req){

           User user = userRepository.findUserByEmail(req.email()).orElseThrow(
                   () -> new RuntimeException("User not found"));

           if(!encoder.matches(req.password(), user.getPasswordHash()))
               throw new RuntimeException("Invalid password");

           return new AuthResponse(jwtService.generateToken(user));

    }

    public User getMe(String email){
        return userRepository.findUserByEmail(email).orElseThrow();
    }

    public User updateProfile(String email, UpdateProfileRequest req){

        User user = getMe(email);

        if(req.name() != null)
            user.setName(req.name());
        if(req.email() != null)
            user.setEmail(req.email());
        if(req.avatarUrl() != null)
            user.setAvatarUrl(req.avatarUrl());



        return userRepository.save(user);
    }
}
