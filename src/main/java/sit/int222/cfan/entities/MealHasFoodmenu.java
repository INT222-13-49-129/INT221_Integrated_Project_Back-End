package sit.int222.cfan.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class MealHasFoodmenu {

  @EmbeddedId
  private MealHasFoodmenuKey key;

  @ManyToOne
  @MapsId("mealMealid")
  @JoinColumn(name = "Meal_Mealid")
  private Meal meal;

  @ManyToOne
  @MapsId("foodmenuFoodmenuid")
  @JoinColumn(name = "Foodmenu_Foodmenuid")
  private Foodmenu foodmenu;

  private long totaldish;
  private long totalkcal;

}
