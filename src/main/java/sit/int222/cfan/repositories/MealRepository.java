package sit.int222.cfan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int222.cfan.model.Meal;

public interface MealRepository extends JpaRepository<Meal,Long> {
}
