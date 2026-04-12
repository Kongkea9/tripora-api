package tripora.api.mapper;

import org.springframework.stereotype.Component;
import tripora.api.domain.Category;
import tripora.api.dto.CategoryResponse;


@Component
public class CategoryMapper {

    public CategoryResponse mapFromCategoryEntityoCategoryResponse(Category category){
        return new CategoryResponse(
                category.getName(),
                category.getSlug(),
                category.getType(),
                category.getIsActive(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
