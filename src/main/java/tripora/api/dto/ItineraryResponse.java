package tripora.api.dto;

import lombok.Builder;

@Builder
public record ItineraryResponse(
        Integer id,
        Integer dayNumber,
        String title,
        String description
) {}