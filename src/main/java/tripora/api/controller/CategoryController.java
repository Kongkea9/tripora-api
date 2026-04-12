package tripora.api.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import tripora.api.dto.CategoryRequest;
import tripora.api.dto.CategoryResponse;
import tripora.api.dto.UpdateCategoryRequest;
import tripora.api.repository.CategoryRepository;
import tripora.api.service.category.CategoryServiceImpl;

@RestController
@RequestMapping("v1/api/categories")
@Slf4j
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }
     @PostMapping
    public CategoryResponse createCategory(@Valid @RequestBody CategoryRequest categoryRequest){
        log.info("created category: {}", categoryRequest);
        return categoryService.createCategory(categoryRequest);
    }

    @GetMapping
    public Page<CategoryResponse> getAll(
            @RequestParam (required = false, defaultValue = "0") int pageNum,
            @RequestParam(required = false, defaultValue = "5") int pageSize
    ){
        return categoryService.getAll(pageNum, pageSize);

    }

    @GetMapping("/{slug}")
    public CategoryResponse getBySlug(
            @PathVariable String slug
    ){
        return categoryService.getBySlug(slug);
    }


    @PutMapping("/{slug}")
    public CategoryResponse updateBySlug(
            @PathVariable String slug,
            @Valid @RequestBody UpdateCategoryRequest updateCategoryRequest
            ){
        return categoryService.updateCategoryBySlug(slug , updateCategoryRequest);
    }


    @DeleteMapping("/{slug}")
    public void deleteBySlug(
            @PathVariable String slug
    ){
        categoryService.deleteBySlug(slug);
    }




}
