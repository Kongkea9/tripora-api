package tripora.api.mapper;

import org.springframework.stereotype.Component;
import tripora.api.domain.TourImage;
import tripora.api.dto.TourImageResponse;


@Component
public class TourImageMapper {

    public TourImageResponse mapFromTourImageEntityToTourImageResponse(TourImage tourImage){
        return new TourImageResponse(
                tourImage.getImageUrl(),
                tourImage.getSortOrder(),
                tourImage.getTour().getId()
        );
    }
}
