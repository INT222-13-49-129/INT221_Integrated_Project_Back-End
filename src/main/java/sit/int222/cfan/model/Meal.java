package sit.int222.cfan.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Meal {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long mealid;
  @Column(columnDefinition = "ENUM('Breakfast', 'Lunch', 'Dinner', 'Lightmeal')")
  @Enumerated(EnumType.STRING)
  private MealTime mealtime;
  private java.sql.Date dateMeal;
  private long totalkcal;
  @ManyToOne
  private User user;

  public enum MealTime {
    Breakfast,Lunch,Dinner,Lightmeal
  }
 }
