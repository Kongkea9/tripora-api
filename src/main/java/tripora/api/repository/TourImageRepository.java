package tripora.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tripora.api.domain.TourImage;
import tripora.api.dto.TourImageResponse;

import java.util.Optional;

public interface TourImageRepository extends JpaRepository<TourImage, Integer> {

       Optional<TourImageResponse> getTourImageById(Integer id);
}
