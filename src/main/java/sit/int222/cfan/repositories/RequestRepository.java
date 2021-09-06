package sit.int222.cfan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int222.cfan.entities.Request;
import sit.int222.cfan.entities.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByUser(User user);

    Request findByUserAndRequestid(User user, Long id);
}
