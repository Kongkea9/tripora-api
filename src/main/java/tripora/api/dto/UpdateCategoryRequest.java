package tripora.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record UpdateCategoryRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 255, message = "Name must not exceed 255 characters")
                String name,

        @NotBlank(message = "Slug is required")
        @Size(max = 255)
        String slug,

        @NotBlank(message = "Type is required")
        @Size(max = 255)
        String type,

        @NotNull(message = "isActive is required")
        Boolean isActive

) {
}
