package sit.int222.cfan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Ingrediansreq {

  @Id
  @Column(name = "Request_Requestid")
  private Long requestRequestid;

  private String ingradianname;
  private long kcalpunit;
  private String unit;
  private String descriptionunit;

  @Column(columnDefinition = "ENUM('Oil', 'Carb', 'Meat', 'Vegetable', 'Fruit', 'Condiment')")
  @Enumerated(EnumType.STRING)
  private IngredianType ingrediantype;

  @JsonBackReference
  @OneToOne
  @MapsId
  @JoinColumn(name = "Request_Requestid")
  private Request request;

}
