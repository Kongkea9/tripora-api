package tripora.api.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record TourImageReorderBatchRequest(

        @NotEmpty
        List<TourImageReorderRequest> items
) {}