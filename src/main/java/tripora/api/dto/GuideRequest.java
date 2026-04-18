package tripora.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import tripora.api.Util.annotation.Phone;

public record GuideRequest(

        @NotBlank(message = "Guide name is required")
        @Size(min = 3, max = 255, message = "Name must be 3–255 characters")
        String name,

        @Size(max = 255, message = "Bio must not exceed 255 characters")
        String bio,

        @Size(max = 500, message = "Photo URL too long")
        @Pattern(regexp = "^(https?://.*)?$", message = "Photo must be a valid URL")
        String photoUrl,

        @Phone
        @NotNull(message = "Phone number is required")
        String phone,

        @NotNull(message = "Availability is required")
        Boolean isAvailable
) {}