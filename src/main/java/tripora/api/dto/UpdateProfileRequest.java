

package tripora.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @Size(max = 255)
        String name,

        @Email
        @Size(max = 255)
        String email,

        @Size(max = 255)
        String avatarUrl
) {}