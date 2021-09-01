package sit.int222.cfan.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Foodmenu {

  @Id
  @GeneratedValue
  private long foodmenuid;
  private String foodname;
  private long totalkcal;
  private String image;
  private String description;

  @Column(columnDefinition = "ENUM('PUBLISH', 'PERSONAL')")
  @Enumerated(EnumType.STRING)
  private FoodmenuStatus foodmenustatus;

  @ManyToOne
  private Foodtype foodtype;

  @JsonBackReference(value = "user")
  @ManyToOne
  private User user;

  @OneToMany(mappedBy = "foodmenu")
  private List<FoodmenuHasIngredients> foodmenuHasIngredientsList;

  @JsonBackReference(value = "mealHasFoodmenuList")
  @OneToMany(mappedBy = "foodmenu",orphanRemoval = true)
  private List<MealHasFoodmenu> mealHasFoodmenuList;

  public enum FoodmenuStatus {
    PUBLISH,PERSONAL
  }
}
