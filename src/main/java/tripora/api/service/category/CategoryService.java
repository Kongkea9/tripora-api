package tripora.api.service.category;

import org.springframework.data.domain.Page;
import tripora.api.domain.Category;
import tripora.api.dto.CategoryRequest;
import tripora.api.dto.CategoryResponse;
import tripora.api.dto.UpdateCategoryRequest;

public interface CategoryService {

    Page<CategoryResponse> getAll(int pageNum, int pageSize);
    CategoryResponse createCategory(CategoryRequest categoryRequest);
    CategoryResponse updateCategoryBySlug(String slug, UpdateCategoryRequest updateCategoryRequest);
    CategoryResponse getBySlug(String slug);
    void deleteBySlug(String slug);


}
