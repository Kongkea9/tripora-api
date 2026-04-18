package tripora.api.dto;

import jakarta.validation.constraints.NotNull;

public record ApproveBookingRequest(

        @NotNull(message = "Guide id is required")
        Integer guideId
) {}
