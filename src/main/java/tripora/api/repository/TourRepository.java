

package tripora.api.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tripora.api.domain.Tour;
import tripora.api.dto.TourFlatProjection;
import tripora.api.dto.TourFlatResponse;

import java.util.Optional;

public interface TourRepository extends JpaRepository<Tour, Integer> {


    @Query("""
SELECT new tripora.api.dto.TourFlatResponse(
    t.id,
    t.title,
    t.description,
    t.durationDay,
    t.durationNight,
    t.whatsIncluded,
    t.whatExcluded,
    t.province,
    t.city,
    t.coverImage,
    t.isActive,
    t.createdAt,
    new tripora.api.dto.CategoryResponse(
        c.id,
        c.name,
        c.slug,
        c.type,
        c.isActive,
        c.createdAt,
        c.updatedAt
    )
)
FROM Tour t
JOIN t.category c
WHERE t.isActive = true
AND (:categorySlug IS NULL OR c.slug = :categorySlug)
    AND (:province IS NULL OR t.province = :province)
    AND (:city IS NULL OR t.city = :city)
""")
    Page<TourFlatResponse> findAllWithFilters(
            String categorySlug,
            String province,
            String city,
            Pageable pageable
    );





    @Query("""
    SELECT new tripora.api.dto.TourFlatResponse(
        t.id,
        t.title,
        t.description,
        t.durationDay,
        t.durationNight,
        t.whatsIncluded,
        t.whatExcluded,
        t.province,
        t.city,
        t.coverImage,
        t.isActive,
        t.createdAt,
        new tripora.api.dto.CategoryResponse(
            c.id,
            c.name,
            c.slug,
            c.type,
            c.isActive,
            c.createdAt,
            c.updatedAt
        )
    )
    FROM Tour t
    JOIN t.category c
    WHERE 
        (:categorySlug IS NULL OR c.slug = :categorySlug)
    AND (:province IS NULL OR t.province = :province)
    AND (:city IS NULL OR t.city = :city)
    """)

    Page<TourFlatResponse> findAllWithFiltersForAdmin(
            String categorySlug,
            String province,
            String city,
            Pageable pageable
    );




    @EntityGraph(attributePaths = {
            "category",
            "tourImages",
            "itineraries",
            "transportOptions"
    })
    Optional<Tour> findWithDetailsById(Integer id);

    boolean existsByCategory_Id(Integer id);


    @Query("""
    SELECT COUNT(t) > 0
    FROM Tour t
    WHERE LOWER(t.title) = LOWER(:title)
    AND t.city = :city
""")
    boolean existsDuplicateTitle(
            @Param("title") String title,
            @Param("city") String city
    );

    @Query("""
    SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
    FROM Tour t
    WHERE t.title = :title
    AND t.city = :city
    AND t.id <> :id
    """)
    boolean existsDuplicateExcludingSelf(String title, String city, Integer id);
}