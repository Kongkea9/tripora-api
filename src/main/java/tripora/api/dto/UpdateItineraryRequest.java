package tripora.api.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateItineraryRequest(

        @Positive(message = "Day number must be a positive number")
        Integer dayNumber,

        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        String description
) {}