package tripora.api.dto;

import java.util.List;


public record TourImageReorderBatchRequest(
        List<Item> items
) {
        public record Item(
                Integer imageId,
                Integer sortOrder
        ) {}
}