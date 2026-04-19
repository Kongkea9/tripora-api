

package tripora.api.dto;

import jakarta.validation.constraints.*;
import tripora.api.Util.annotation.Phone;

public record UpdateProfileRequest(


        @NotBlank(message = "Name is required")
        @Size(max = 255)
        String name,

        @Phone
        @NotNull(message = "Phone number is required")
        String phone,

        @NotBlank(message = "Email is required")
        @Email(message = "Email format is invalid")
        @Size(max = 255)
        String email,

        @Size(max = 255)
        String avatarUrl
) {}