package tripora.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tripora.api.domain.Guide;

public interface GuideRepository extends JpaRepository<Guide, Integer> {
    boolean existsByPhone(String phone);
}