package sit.int222.cfan.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class FoodmenuHasIngredians {

  @EmbeddedId
  FoodmenuHasIngrediansKey key;

  @ManyToOne
  @MapsId("foodmenuFoodmenuid")
  @JoinColumn(name = "Foodmenu_Foodmenuid")
  Foodmenu foodmenu;

  @ManyToOne
  @MapsId("ingrediansIngradiansid")
  @JoinColumn(name = "Ingredians_Ingradiansid")
  Ingredians ingredians;

  private long totalunit;
  private long totalkcal;

}
