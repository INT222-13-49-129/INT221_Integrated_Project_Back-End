package sit.int222.cfan.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class MealHasFoodmenuKey implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6390941505869333959L;
	@Column(name = "Meal_Mealid")
    private long mealMealid;
    @Column(name = "Foodmenu_Foodmenuid")
    private long foodmenuFoodmenuid;

}
