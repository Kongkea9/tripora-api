package tripora.api.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record BookingRequest(

        @NotNull
        Integer tourId,

        @NotNull
        @FutureOrPresent
        LocalDate travelDate,

        Integer transportOptionId,

        Integer guideId,

        @NotNull
        @Min(1)
        @Max(20)
        Integer guestCount
) {}