package sit.int222.cfan.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long mealid;

    @Column(columnDefinition = "ENUM('Breakfast', 'Lunch', 'Dinner', 'Lightmeal')")
    @Enumerated(EnumType.STRING)
    private MealTime mealtime;
    private java.sql.Date datemeal;
    private long totalkcal;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "meal")
    List<MealHasFoodmenu> mealHasFoodmenuList;

    public enum MealTime {
        Breakfast, Lunch, Dinner, Lightmeal
    }
}
