package sit.int222.cfan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int222.cfan.entities.Meal;
import sit.int222.cfan.entities.User;

import java.sql.Date;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findAllByUser(User user);

    Meal findByUserAndMealid(User user, Long id);

    List<Meal> findByUserAndDatemeal(User user, Date date);

    List<Meal> findByDatemeal(Date date);
}
