package tripora.api.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransportOptionResponse(
        Integer id,
        String vehicleType,
        Integer minGuests,
        Integer maxGuests,
        BigDecimal groupPrice,
        String note
) {}