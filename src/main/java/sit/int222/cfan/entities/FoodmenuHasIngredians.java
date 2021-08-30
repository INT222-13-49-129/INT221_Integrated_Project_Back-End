package sit.int222.cfan.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class FoodmenuHasIngredians {

  @EmbeddedId
  private FoodmenuHasIngrediansKey key;

  @JsonBackReference(value="foodmenuHasIngrediansList")
  @ManyToOne
  @MapsId("foodmenuFoodmenuid")
  @JoinColumn(name = "Foodmenu_Foodmenuid")
  private Foodmenu foodmenu;

  @ManyToOne
  @MapsId("ingrediansIngradiansid")
  @JoinColumn(name = "Ingredians_Ingradiansid")
  private Ingredians ingredians;

  private long totalunit;
  private long totalkcal;

}
