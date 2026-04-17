package tripora.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ItineraryRequest(

        @NotNull(message = "Day number is required")
        @Positive(message = "Day number must be a positive number")
        Integer dayNumber,

        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        String description
) {}