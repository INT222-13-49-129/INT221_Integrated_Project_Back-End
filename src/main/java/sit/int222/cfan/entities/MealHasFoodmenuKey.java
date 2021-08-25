package sit.int222.cfan.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class MealHasFoodmenuKey implements Serializable {

    @Column(name = "Meal_Mealid")
    private long mealMealid;
    @Column(name = "Foodmenu_Foodmenuid")
    private long foodmenuFoodmenuid;

}
