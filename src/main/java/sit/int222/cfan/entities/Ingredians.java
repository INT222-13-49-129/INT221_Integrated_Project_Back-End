package sit.int222.cfan.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ingredians {

    @Id
    @GeneratedValue
    private long ingradiansid;
    private String ingradianname;
    private long kcalpunit;
    private String unit;
    private String descriptionunit;

    @Column(columnDefinition = "ENUM('Oil', 'Carb', 'Meat', 'Vegetable', 'Fruit', 'Condiment')")
    @Enumerated(EnumType.STRING)
    private IngredianType ingrediantype;

    @JsonBackReference(value = "foodmenuHasIngrediansList")
    @OneToMany(mappedBy = "ingredians")
    private List<FoodmenuHasIngredians> foodmenuHasIngrediansList;

}
