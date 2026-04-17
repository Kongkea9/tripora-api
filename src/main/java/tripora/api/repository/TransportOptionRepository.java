package tripora.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tripora.api.domain.TransportOption;

import java.util.List;
import java.util.Optional;

public interface TransportOptionRepository extends JpaRepository<TransportOption, Integer> {

    List<TransportOption> findByTourId(Integer tourId);

    Optional<TransportOption> findByIdAndTourId(Integer id, Integer tourId);
}