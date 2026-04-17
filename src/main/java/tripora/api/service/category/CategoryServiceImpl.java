package tripora.api.service.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tripora.api.domain.Category;
import tripora.api.domain.Tour;
import tripora.api.dto.CategoryRequest;
import tripora.api.dto.CategoryResponse;
import tripora.api.dto.UpdateCategoryRequest;
import tripora.api.exception.ConflictException;
import tripora.api.exception.ResourceNotFoundException;
import tripora.api.mapper.CategoryMapper;
import tripora.api.repository.CategoryRepository;
import tripora.api.repository.TourRepository;

import java.time.LocalDate;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final TourRepository tourRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper, TourRepository tourRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.tourRepository = tourRepository;
    }


    @Override
    public Page<CategoryResponse> getAll(int pageNum, int pageSize) {

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return categoryRepository.findAll(pageable)
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
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


           if(categoryRepository.findCategoriesBySlug(categoryRequest.slug()).isPresent())
               throw new ConflictException("Slug '" + categoryRequest.slug() + "' already exists");


           Category category = new Category();
           category.setName(categoryRequest.name());
           category.setSlug(categoryRequest.slug());
           category.setType(categoryRequest.type());
           category.setIsActive(categoryRequest.isActive());
           category.setCreatedAt(LocalDate.now());
           category.setUpdatedAt(LocalDate.now());

           categoryRepository.save(category);

        return categoryMapper.mapFromCategoryEntityoCategoryResponse(category);
    }

    @Override
    public CategoryResponse updateCategoryBySlug(String slug, UpdateCategoryRequest updateCategoryRequest) {

        Category foundCategory = categoryRepository.findCategoriesBySlug(slug)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category with slug '" + slug + "' not found")
                );


        if(categoryRepository.findCategoriesBySlug(updateCategoryRequest.slug()).isPresent())
            throw new ConflictException("Slug '" + updateCategoryRequest.slug() + "' already exists");

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


        foundCategory.setUpdatedAt(LocalDate.now());


        Category updateCategory = categoryRepository.save(foundCategory);



        return categoryMapper.mapFromCategoryEntityoCategoryResponse(updateCategory);
    }

    @Override
    public CategoryResponse getBySlug(String slug) {


        Category foundCategory = categoryRepository.findCategoriesBySlug(slug)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category with slug '" + slug + "' not found"));

        return categoryMapper.mapFromCategoryEntityoCategoryResponse(foundCategory);
    }

//    @Override
//    public void deleteBySlug(String slug) {
//
//        Category foundCategory = categoryRepository.findCategoriesBySlug(slug)
//                .orElseThrow(() -> new ResourceNotFoundException("Category with slug '" + slug + "' not found")
//                );
//
////        categoryRepository.delete(foundCategory);
//
//        foundCategory.setIsActive(false);
//        categoryRepository.save(foundCategory);
//
//    }



@Override
public void deleteBySlug(String slug) {

    Category foundCategory = categoryRepository.findCategoriesBySlug(slug)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Category with slug '" + slug + "' not found"
            ));



    if (tourRepository.existsByCategory_Id(foundCategory.getId())) {
        throw new ConflictException("Cannot delete category. It is assigned to existing tours.");
    }

    foundCategory.setIsActive(false);
    categoryRepository.save(foundCategory);
}



}
