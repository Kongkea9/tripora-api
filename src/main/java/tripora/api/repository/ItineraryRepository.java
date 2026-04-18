package tripora.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tripora.api.domain.Itinerary;

import java.util.Optional;

public interface ItineraryRepository extends JpaRepository<Itinerary, Integer> {
    Optional<Itinerary> findByIdAndTourId(Integer id, Integer tourId);
    boolean existsByTourIdAndDayNumber(Integer tourId, Integer dayNumber);

    @Query("SELECT MAX(i.dayNumber) FROM Itinerary i WHERE i.tour.id = :tourId")
    Optional<Long> findMaxDayNumberByTourId(@Param("tourId") Integer tourId);
}