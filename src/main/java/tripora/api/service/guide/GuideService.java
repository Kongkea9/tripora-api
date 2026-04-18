package tripora.api.service.guide;

import tripora.api.dto.GuideRequest;
import tripora.api.dto.GuideResponse;

import java.util.List;

public interface GuideService {

    List<GuideResponse> getAll();

    GuideResponse create(GuideRequest req);

    GuideResponse update(Integer id, GuideRequest req);

    void delete(Integer id);
}
