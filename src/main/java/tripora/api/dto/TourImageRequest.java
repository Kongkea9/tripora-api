package tripora.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TourImageRequest(

        @NotBlank(message = "Image URL must not be blank")
        @Pattern(
                regexp = "^(https?|ftp)://.*$",
                message = "Image URL must be valid"
        )
        @Size(max = 500, message = "Image URL must not exceed 500 characters")
        String imageUrl,

        @NotNull(message = "Sort order must not be null")
        @Min(value = 0, message = "Sort order must be 0 or greater")
        Integer sortOrder
) {
}