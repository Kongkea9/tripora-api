package tripora.api.dto;

import java.time.LocalDate;

public record TourFlatResponse(
        Integer id,
        String title,
        String description,
        Integer durationDay,
        Integer durationNight,
        String whatsIncluded,
        String whatExcluded,
        String province,
        String city,
        String coverImage,
        Boolean isActive,
        LocalDate createdAt,
        CategoryResponse category
) {}