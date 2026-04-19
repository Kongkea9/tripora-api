
package tripora.api.dto;

import jakarta.validation.constraints.*;
import tripora.api.Util.annotation.Phone;

public record RegisterRequest(
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

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 255, message = "Password must be at least 8 characters")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,255}$",
                message = "Password must contain uppercase, lowercase, number and special character"
        )
        String password


) {}
