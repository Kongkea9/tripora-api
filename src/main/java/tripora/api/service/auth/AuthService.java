package tripora.api.service.auth;

import tripora.api.domain.User;
import tripora.api.dto.AuthResponse;
import tripora.api.dto.LoginRequest;
import tripora.api.dto.RegisterRequest;
import tripora.api.dto.UpdateProfileRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest req);
    AuthResponse login(LoginRequest req);
    User getMe(String email);
    User updateProfile(String email, UpdateProfileRequest req);



}
