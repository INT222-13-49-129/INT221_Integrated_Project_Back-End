package sit.int222.cfan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int222.cfan.entities.Ingredients;

public interface IngredientsRepository extends JpaRepository<Ingredients, Long> {
}
