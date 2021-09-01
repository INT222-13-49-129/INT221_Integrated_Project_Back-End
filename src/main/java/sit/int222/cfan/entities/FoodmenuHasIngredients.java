package sit.int222.cfan.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class FoodmenuHasIngredients {

  @EmbeddedId
  private FoodmenuHasIngredientsKey key;

  @JsonBackReference(value="foodmenuHasIngredientsList")
  @ManyToOne
  @MapsId("foodmenuFoodmenuid")
  @JoinColumn(name = "Foodmenu_Foodmenuid")
  private Foodmenu foodmenu;

  @ManyToOne
  @MapsId("ingredientsIngredientsid")
  @JoinColumn(name = "Ingredients_Ingredientsid")
  private Ingredients ingredients;

  private long totalunit;
  private long totalkcal;

}
