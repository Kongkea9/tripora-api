package tripora.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record TourImageRequest(

        @NotBlank(message ="Image url must be not blank")
        @URL(message = "Image URL must me a valid URL")
        @Size(max = 500, message = "Image URL must not exceed 500 characters")
        String imgUrl,

        @NotNull(message = "Sort order must not be null")
        @Min(value = 0, message = "Sort order must be o or greater")
        Integer sortOrder,

        @NotNull(message = "Tour Id must not be null")
        Integer tourId
) {
}
