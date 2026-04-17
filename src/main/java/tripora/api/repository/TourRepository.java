

package tripora.api.repository;

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
SELECT t FROM Tour t
LEFT JOIN FETCH t.category
LEFT JOIN FETCH t.tourImages
LEFT JOIN FETCH t.itineraries
LEFT JOIN FETCH t.transportOptions
WHERE t.id = :id
""")
    Optional<Tour> findWithDetailsById(@Param("id") Integer id);

    boolean existsByCategory_Id(Integer id);
}