package sit.int222.cfan.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class MealHasFoodmenu {

    @EmbeddedId
    private MealHasFoodmenuKey key;

    @JsonBackReference(value = "meal-mealhasfoodmenu")
    @ManyToOne
    @MapsId("mealMealid")
    @JoinColumn(name = "Meal_Mealid")
    private Meal meal;

    @JsonBackReference(value = "foodmenu-mealhasfoodmenu")
    @ManyToOne
    @MapsId("foodmenuFoodmenuid")
    @JoinColumn(name = "Foodmenu_Foodmenuid")
    private Foodmenu foodmenu;

    private long totaldish;
    private long totalkcal;

}
