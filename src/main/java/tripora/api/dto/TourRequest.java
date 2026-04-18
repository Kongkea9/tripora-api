package tripora.api.dto;

import jakarta.validation.constraints.*;



public record TourRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        String description,

        @NotNull(message = "Duration days is required")
        @Positive(message = "Duration days must be a positive number")
        Integer durationDay,

        @NotNull(message = "Duration nights is required")
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
        @NotBlank(message = "Cover image is required")
        @Size(max = 500, message = "Cover image URL must not exceed 500 characters")
        String coverImage,

        @NotNull(message = "isActive is required")
        Boolean isActive,


        @NotNull(message = "Category ID is required")
        Integer categoryId
) {}