package sit.int222.cfan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int222.cfan.entities.Jwtblacklist;

import java.sql.Timestamp;
import java.util.List;

public interface JwtblacklistRepository extends JpaRepository<Jwtblacklist, Long> {
    boolean existsByToken(String token);

    List<Jwtblacklist> findAllByExpLessThan(Timestamp time);
}
