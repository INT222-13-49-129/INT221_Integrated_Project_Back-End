package sit.int222.cfan.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class MealHasFoodmenu {

  @EmbeddedId
  MealHasFoodmenuKey key;

  @ManyToOne
  @MapsId("mealMealid")
  @JoinColumn(name = "Meal_Mealid")
  Meal meal;

  @ManyToOne
  @MapsId("foodmenuFoodmenuid")
  @JoinColumn(name = "Foodmenu_Foodmenuid")
  Foodmenu foodmenu;

  private long totaldish;
  private long totalkcal;

}
