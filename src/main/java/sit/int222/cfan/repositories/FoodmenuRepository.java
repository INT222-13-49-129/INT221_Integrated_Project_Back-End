package sit.int222.cfan.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.int222.cfan.entities.Foodmenu;

import java.util.List;

public interface FoodmenuRepository extends JpaRepository<Foodmenu, Long> {
    List<Foodmenu> findAllByFoodmenustatus(Foodmenu.FoodmenuStatus foodmenuStatus);

    Page<Foodmenu> findAllByFoodmenustatus(Foodmenu.FoodmenuStatus foodmenuStatus, Pageable pageable);

    @Query(value = "SELECT f FROM Foodmenu as f where f.foodmenustatus = ?1 and (f.foodname like %?2% or f.description like %?2%)")
    Page<Foodmenu> findSearch(Foodmenu.FoodmenuStatus foodmenuStatus, String searchData, Pageable pageable);

    @Query("select f from Foodmenu as f where f.foodmenustatus = ?1 and f.foodtype.foodtypeid = ?2")
    Page<Foodmenu> findAllByFoodtypeId(Foodmenu.FoodmenuStatus foodmenuStatus,Long foodtypeId,Pageable pageable);

    Foodmenu findByFoodmenuidAndFoodmenustatus(Long id, Foodmenu.FoodmenuStatus foodmenuStatus);
}
