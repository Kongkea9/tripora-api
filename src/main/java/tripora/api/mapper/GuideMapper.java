package tripora.api.mapper;

import org.springframework.stereotype.Component;
import tripora.api.domain.Guide;
import tripora.api.dto.GuideResponse;

@Component
public class GuideMapper {

    public GuideResponse mapToResponse(Guide g) {
        return new GuideResponse(
                g.getId(),
                g.getName(),
                g.getBio(),
                g.getPhotoUrl(),
                g.getPhone(),
                g.getIsAvailable()
        );
    }

}
