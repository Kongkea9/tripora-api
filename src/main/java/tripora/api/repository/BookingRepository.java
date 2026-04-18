package tripora.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tripora.api.domain.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByUserId(Integer userId);

    List<Booking> findByTourId(Integer tourId);

    List<Booking> findByStatus(String status);

    boolean existsByGuideIdAndTravelDate(Integer guideId, LocalDate travelDate);
}