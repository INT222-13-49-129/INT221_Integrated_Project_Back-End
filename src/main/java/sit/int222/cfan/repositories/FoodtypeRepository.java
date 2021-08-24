package sit.int222.cfan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int222.cfan.model.Foodtype;

public interface FoodtypeRepository extends JpaRepository<Foodtype,Long> {
}
