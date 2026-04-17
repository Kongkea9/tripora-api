package tripora.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tripora.api.domain.TourImage;

import java.util.List;
import java.util.Optional;

public interface TourImageRepository extends JpaRepository<TourImage, Integer> {


       List<TourImage> findByTourIdOrderBySortOrderAsc(Integer tourId);

       boolean existsByTourIdAndSortOrder(Integer tourId, Integer sortOrder);

       Optional<TourImage> findByIdAndTourId(Integer imageId, Integer tourId);
}