package tripora.api.dto;


import lombok.Builder;

@Builder
public record TourImageResponse(

        String imgUrl,
        Integer sortOrder,
        Integer tourId
) {
}
