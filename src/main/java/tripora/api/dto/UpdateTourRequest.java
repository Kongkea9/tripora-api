package tripora.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateTourRequest(

        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        String description,

        @Positive(message = "Duration days must be a positive number")
        Integer durationDay,

        @Positive(message = "Duration nights must be a positive number")
        Integer durationNight,

        String whatsIncluded,

        String whatExcluded,

        @Size(max = 255, message = "Province must not exceed 255 characters")
        String province,

        @Size(max = 255, message = "City must not exceed 255 characters")
        String city,

        @Pattern(
                regexp = "^(https?|ftp)://.*$",
                message = "Image URL must be valid"
        )
        @Size(max = 500, message = "Cover image URL must not exceed 500 characters")
        String coverImage,


        @NotNull(message = "isActive is required")
        Boolean isActive,

        @NotNull(message = "Category ID is required")
        Integer categoryId
) {}