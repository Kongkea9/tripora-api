package tripora.api.dto;

import jakarta.validation.constraints.NotNull;

public record TourImageReorderRequest(

        @NotNull Integer imageId,
        @NotNull Integer sortOrder
) {}