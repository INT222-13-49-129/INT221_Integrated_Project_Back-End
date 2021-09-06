package sit.int222.cfan.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Ingredientsreq {

    @Id
    @Column(name = "Request_Requestid")
    private Long requestRequestid;

    private String ingredientsname;
    private long kcalpunit;
    private String unit;
    private String descriptionunit;

    @Column(columnDefinition = "ENUM('Oil', 'Carb', 'Meat', 'Vegetable', 'Fruit', 'Condiment')")
    @Enumerated(EnumType.STRING)
    private IngredientsType ingredientstype;

    @JsonBackReference
    @OneToOne
    @MapsId
    @JoinColumn(name = "Request_Requestid")
    private Request request;

}
