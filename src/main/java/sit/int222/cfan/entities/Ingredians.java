package sit.int222.cfan.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
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

    @JsonBackReference
    @OneToMany(mappedBy = "ingredians")
    List<FoodmenuHasIngredians> foodmenuHasIngrediansList;

}
