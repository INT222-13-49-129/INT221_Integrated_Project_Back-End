package sit.int222.cfan.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class User {

  @Id
  @GeneratedValue
  private long userid;
  private String username;
  private String email;

  @JsonIgnore
  private String password;
  private String firstname;
  private String lastname;
  private java.sql.Date doB;

  @Column(columnDefinition = "ENUM('M', 'F')")
  @Enumerated(EnumType.STRING)
  private Gender gender;
  private double weight;
  private double height;

  @Column(columnDefinition = "ENUM('NORMAL', 'ADMIN')")
  @Enumerated(EnumType.STRING)
  private Status status;

  @JsonBackReference
  @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
  private List<Foodmenu> foodmenus;

  @JsonBackReference
  @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
  private List<Meal> meals;

  @JsonBackReference
  @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
  private List<Request> requests;

  @JsonBackReference
  @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
  private List<Jwtblacklist> jwtblacklists;

  public enum Gender {
    M,F,
  }

  public enum Status {
    NORMAL,ADMIN
  }

}
