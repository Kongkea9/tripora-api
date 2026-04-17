package tripora.api.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CategoryResponse(

        Integer id,
        String name,
        String slug,
        String type,
        Boolean isActive,
        LocalDate createdAt,
        LocalDate updatedAt


) {
}
