package sit.int222.cfan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Foodtype {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long foodtypeid;
  private String typename;

  @JsonBackReference
  @OneToMany(mappedBy = "foodtype",fetch = FetchType.LAZY)
  private List<Foodmenu> foodmenus;

}
