package sit.int222.cfan.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sit.int222.cfan.entities.Ingredients;
import sit.int222.cfan.entities.IngredientsType;

public interface IngredientsRepository extends JpaRepository<Ingredients, Long> {
    Page<Ingredients> findAllByIngredientsnameContaining(String searchData,Pageable pageable);

    Page<Ingredients> findAllByIngredientsnameContainingAndIngredientstype(String searchData, IngredientsType type,Pageable pageable);
}
