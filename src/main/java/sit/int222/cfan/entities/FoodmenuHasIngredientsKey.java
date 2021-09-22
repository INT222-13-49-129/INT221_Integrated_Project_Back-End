package sit.int222.cfan.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class FoodmenuHasIngredientsKey implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2875387016069320017L;
	@Column(name = "Foodmenu_Foodmenuid")
    private long foodmenuFoodmenuid;
    @Column(name = "Ingredients_Ingredientsid")
    private long ingredientsIngredientsid;
}
