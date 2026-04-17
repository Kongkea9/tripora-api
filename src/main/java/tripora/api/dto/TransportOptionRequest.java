package tripora.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record TransportOptionRequest(

        @NotBlank(message = "Vehicle type is required")
        @Size(min = 2, max = 100, message = "Vehicle type must be between 2 and 100 characters")
        String vehicleType,

        @NotNull(message = "Minimum guests is required")
        @Min(value = 1, message = "Minimum guests must be at least 1")
        Integer minGuests,

        @NotNull(message = "Maximum guests is required")
        @Min(value = 1, message = "Maximum guests must be at least 1")
        Integer maxGuests,

        @NotNull(message = "Group price is required")
        @DecimalMin(value = "0.01", message = "Group price must be greater than 0")
        BigDecimal groupPrice,

        @Size(max = 500, message = "Note cannot exceed 500 characters")
        String note
) {}