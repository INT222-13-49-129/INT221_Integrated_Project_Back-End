package sit.int222.cfan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int222.cfan.entities.Pin;

public interface PinRepository extends JpaRepository<Pin, Long> {
    Pin findByEmail(String email);
}
