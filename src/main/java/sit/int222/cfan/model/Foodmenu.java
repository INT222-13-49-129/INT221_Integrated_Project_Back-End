package sit.int222.cfan.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Foodmenu {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long foodmenuid;
  private String foodname;
  private long totalkal;
  private String image;
  private String description;

  @Column(columnDefinition = "ENUM('PUBLISH', 'PERSONAL')")
  @Enumerated(EnumType.STRING)
  private FoodmenuStatus foodmenustatus;

  @ManyToOne
  private Foodtype foodtype;
  @ManyToOne
  private User user;

  @OneToMany(mappedBy = "foodmenu")
  List<FoodmenuHasIngredians> foodmenuHasIngrediansList;

  public enum FoodmenuStatus {
    PUBLISH,PERSONAL
  }
}
