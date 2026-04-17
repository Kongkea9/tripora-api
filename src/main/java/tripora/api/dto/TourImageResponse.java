package tripora.api.dto;


import lombok.Builder;

@Builder
public record TourImageResponse(

        Integer id,
        String imageUrl,
        Integer sortOrder,
        Integer tourId
) {
}
