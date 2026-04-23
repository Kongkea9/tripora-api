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
import tripora.api.exception.BadRequestException;
import tripora.api.exception.ConflictException;
import tripora.api.exception.ResourceNotFoundException;
import tripora.api.repository.RoleRepository;
import tripora.api.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;


    @Override
    public AuthResponse register(RegisterRequest req) {
        if(userRepository.existsByEmail(req.email())) {
            throw new ConflictException("Email already registered");
        }

        String phone = normalizePhone(req.phone());

        if (userRepository.existsByPhone(phone)) {
            throw new ConflictException("Phone already exists");
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
        user.setPhone(phone);
        user.setRoles(Set.of(userRole));
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());


        userRepository.save(user);

        return new AuthResponse(jwtService.generateToken(user));
    }


    @Override
    public AuthResponse login(LoginRequest req){

           User user = userRepository.findUserByEmail(req.email()).orElseThrow(
                   () -> new ResourceNotFoundException("User not found"));

           if(!encoder.matches(req.password(), user.getPasswordHash()))
               throw new BadRequestException("Invalid password");

           return new AuthResponse(jwtService.generateToken(user));

    }

    @Override
    public User getMe(String email){

        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }


    @Override
    public User updateProfile(String email, UpdateProfileRequest req){

        User user = getMe(email);


        String phone = normalizePhone(req.phone());

        if (userRepository.existsByPhone(phone)) {
            throw new ConflictException("Phone already exists");
        }

        if(req.name() != null)
            user.setName(req.name());

        if(req.phone() != null)
            user.setPhone(phone);

        if (req.email() != null) {
            if (userRepository.existsByEmailAndIdNot(req.email(), user.getId())) {
                throw new ConflictException("Email already exists");
            }
            user.setEmail(req.email());
        }
        if(req.avatarUrl() != null)
            user.setAvatarUrl(req.avatarUrl());

        user.setUpdatedAt(LocalDate.now());

        if (req.phone() != null && userRepository.existsByPhone(user.getPhone())) {
            throw new ConflictException("Phone already exists");
        }


        return userRepository.save(user);
    }



    private String normalizePhone(String value) {
        if (value == null) return null;

        String phone = value.replaceAll("[\\s-]", "");

        if (phone.startsWith("+855")) {
            phone = phone.substring(4);
        } else if (phone.startsWith("0")) {
            phone = phone.substring(1);
        }

        return "+855" + phone;
    }
}
