package tripora.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tripora.api.domain.Itinerary;

import java.util.Optional;

public interface ItineraryRepository extends JpaRepository<Itinerary, Integer> {
    Optional<Itinerary> findByIdAndTourId(Integer id, Integer tourId);
    boolean existsByTourIdAndDayNumber(Integer tourId, Integer dayNumber);
}