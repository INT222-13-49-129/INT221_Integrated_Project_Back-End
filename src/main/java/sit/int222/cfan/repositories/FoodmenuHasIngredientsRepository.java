package sit.int222.cfan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int222.cfan.entities.FoodmenuHasIngredients;
import sit.int222.cfan.entities.FoodmenuHasIngredientsKey;
import sit.int222.cfan.entities.Ingredients;

import java.util.List;

public interface FoodmenuHasIngredientsRepository extends JpaRepository<FoodmenuHasIngredients, FoodmenuHasIngredientsKey> {
    List<FoodmenuHasIngredients> findAllByIngredients(Ingredients ingredients);
}
