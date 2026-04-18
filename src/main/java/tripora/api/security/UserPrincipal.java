package tripora.api.security;

import java.util.List;

public record UserPrincipal(
        Integer id,
        String email,
        List<String> roles
) {}