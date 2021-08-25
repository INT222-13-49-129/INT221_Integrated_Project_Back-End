package sit.int222.cfan.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Foodtypereq {

  @Id
  @Column(name = "Request_Requestid")
  private Long requestRequestid;

  private String typename;

  @JsonBackReference
  @OneToOne
  @MapsId
  @JoinColumn(name = "Request_Requestid")
  private Request request;

}
