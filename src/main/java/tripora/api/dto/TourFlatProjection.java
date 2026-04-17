package tripora.api.dto;

import java.time.LocalDate;

public record TourFlatProjection(
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
        Integer categoryId,
        String categoryName,
        String categorySlug
) {}