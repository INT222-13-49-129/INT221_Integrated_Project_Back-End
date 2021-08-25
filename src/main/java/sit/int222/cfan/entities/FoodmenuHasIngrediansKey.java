package sit.int222.cfan.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class FoodmenuHasIngrediansKey implements Serializable{

    @Column(name = "Foodmenu_Foodmenuid")
    private long foodmenuFoodmenuid;
    @Column(name = "Ingredians_Ingradiansid")
    private long ingrediansIngradiansid;
}
