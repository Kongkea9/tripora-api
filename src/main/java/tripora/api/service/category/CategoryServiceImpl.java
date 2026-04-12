package tripora.api.service.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tripora.api.domain.Category;
import tripora.api.dto.CategoryRequest;
import tripora.api.dto.CategoryResponse;
import tripora.api.dto.UpdateCategoryRequest;
import tripora.api.mapper.CategoryMapper;
import tripora.api.repository.CategoryRepository;

import java.time.LocalDate;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


    @Override
    public Page<CategoryResponse> getAll(int pageNum, int pageSize) {

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return categoryRepository.findAll(pageable)
                .map(category -> CategoryResponse.builder()
                        .name(category.getName())
                        .slug(category.getSlug())
                        .type(category.getType())
                        .isActive(category.getIsActive())
                        .createdAt(LocalDate.now())
                        .createdAt(LocalDate.now())
                        .build());


    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {


           Category category = new Category();
           category.setName(categoryRequest.name());
           category.setSlug(categoryRequest.slug());
           category.setType(categoryRequest.type());
           category.setIsActive(categoryRequest.isActive());
           category.setCreatedAt(categoryRequest.createdAt());
           category.setUpdatedAt(categoryRequest.updatedAt());

           categoryRepository.save(category);

        return categoryMapper.mapFromCategoryEntityoCategoryResponse(category);
    }

    @Override
    public CategoryResponse updateCategoryBySlug(String slug, UpdateCategoryRequest updateCategoryRequest) {

        Category foundCategory = categoryRepository.findCategoriesBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Category with slug: %s not found", slug )
                ));

        if(updateCategoryRequest.name() != null){
            foundCategory.setName(updateCategoryRequest.name());
        }
        if(updateCategoryRequest.slug() != null){
            foundCategory.setSlug(updateCategoryRequest.slug());
        }
        if(updateCategoryRequest.type() != null){
            foundCategory.setType(updateCategoryRequest.type());
        }

        if(updateCategoryRequest.isActive() != null){
            foundCategory.setIsActive(updateCategoryRequest.isActive());
        }

        if(updateCategoryRequest.updatedAt() != null){
            foundCategory.setUpdatedAt(updateCategoryRequest.updatedAt());
        }

        Category updateCategory = categoryRepository.save(foundCategory);



        return categoryMapper.mapFromCategoryEntityoCategoryResponse(updateCategory);
    }

    @Override
    public CategoryResponse getBySlug(String slug) {


        Category foundCategory = categoryRepository.findCategoriesBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Category with slug: %s not found", slug )
                ));

        return categoryMapper.mapFromCategoryEntityoCategoryResponse(foundCategory);
    }

    @Override
    public void deleteBySlug(String slug) {

        Category foundCategory = categoryRepository.findCategoriesBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Category with slug: %s not found", slug )
                ));

        categoryRepository.delete(foundCategory);

    }
}
