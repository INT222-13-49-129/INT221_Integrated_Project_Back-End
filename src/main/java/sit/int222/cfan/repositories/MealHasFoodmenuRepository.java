package sit.int222.cfan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int222.cfan.entities.MealHasFoodmenu;
import sit.int222.cfan.entities.MealHasFoodmenuKey;

public interface MealHasFoodmenuRepository extends JpaRepository<MealHasFoodmenu, MealHasFoodmenuKey> {
}
