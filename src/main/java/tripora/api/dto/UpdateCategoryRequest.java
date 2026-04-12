package tripora.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateCategoryRequest(
        @NotBlank
        @Size(max = 255)
        String name,


        @NotBlank
        @Size(max = 255)
        String slug,

        @NotBlank
        @Size(max = 255)
        String type,

        @NotNull
        Boolean isActive,


        @NotNull
        LocalDate updatedAt
) {
}
