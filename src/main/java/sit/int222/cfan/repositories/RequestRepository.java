package sit.int222.cfan.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sit.int222.cfan.entities.Request;
import sit.int222.cfan.entities.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByUser(User user);

    Page<Request> findAllByUser(User user, Pageable pageable);

    Request findByUserAndRequestid(User user, Long id);
}
