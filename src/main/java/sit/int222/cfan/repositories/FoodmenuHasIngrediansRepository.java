package sit.int222.cfan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int222.cfan.entities.FoodmenuHasIngredians;
import sit.int222.cfan.entities.FoodmenuHasIngrediansKey;

public interface FoodmenuHasIngrediansRepository extends JpaRepository<FoodmenuHasIngredians, FoodmenuHasIngrediansKey> {
}
