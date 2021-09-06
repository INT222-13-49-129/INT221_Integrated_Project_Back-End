package sit.int222.cfan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int222.cfan.entities.Jwtblacklist;

public interface JwtblacklistRepository extends JpaRepository<Jwtblacklist, Long> {
    boolean existsByToken(String token);
}
