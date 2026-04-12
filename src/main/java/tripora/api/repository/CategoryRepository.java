package tripora.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tripora.api.domain.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findCategoriesBySlug(String slug);

}
