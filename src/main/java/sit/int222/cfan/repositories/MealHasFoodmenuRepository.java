package sit.int222.cfan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int222.cfan.model.MealHasFoodmenu;
import sit.int222.cfan.model.MealHasFoodmenuKey;

public interface MealHasFoodmenuRepository extends JpaRepository<MealHasFoodmenu, MealHasFoodmenuKey> {
}
