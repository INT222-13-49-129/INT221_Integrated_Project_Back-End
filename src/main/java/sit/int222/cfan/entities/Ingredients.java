package sit.int222.cfan.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ingredients {

    @Id
    @GeneratedValue
    private long ingredientsid;
    private String ingredientsname;
    private long kcalpunit;
    private String unit;
    private String descriptionunit;

    @Column(columnDefinition = "ENUM('Oil', 'Carb', 'Meat', 'Vegetable', 'Fruit', 'Condiment')")
    @Enumerated(EnumType.STRING)
    private IngredientsType ingredientstype;

    @JsonBackReference(value = "foodmenuHasIngredientsList")
    @OneToMany(mappedBy = "ingredients")
    private List<FoodmenuHasIngredients> foodmenuHasIngredientsList;

}
