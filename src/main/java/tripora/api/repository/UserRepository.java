package tripora.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tripora.api.domain.User;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> findUserByEmail(@Param("email") String email);

    Boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByEmailAndIdNot(String email, Integer id);
}
