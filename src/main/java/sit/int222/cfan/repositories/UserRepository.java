package sit.int222.cfan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int222.cfan.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
}
