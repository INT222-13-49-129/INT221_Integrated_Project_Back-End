package sit.int222.cfan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int222.cfan.entities.FoodmenuHasIngredients;
import sit.int222.cfan.entities.FoodmenuHasIngredientsKey;

public interface FoodmenuHasIngredientsRepository extends JpaRepository<FoodmenuHasIngredients, FoodmenuHasIngredientsKey> {
}
