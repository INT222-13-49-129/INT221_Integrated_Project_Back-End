package sit.int222.cfan.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Meal {

    @Id
    @GeneratedValue
    private long mealid;

    @Column(columnDefinition = "ENUM('Breakfast', 'Lunch', 'Dinner', 'Lightmeal')")
    @Enumerated(EnumType.STRING)
    private MealTime mealtime;
    private java.sql.Date datemeal;
    private long totalkcal;

    @JsonBackReference
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "meal" )
    private List<MealHasFoodmenu> mealHasFoodmenuList;

    public enum MealTime {
        Breakfast, Lunch, Dinner, Lightmeal
    }
}
