package tripora.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tripora.api.domain.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String name);
}
