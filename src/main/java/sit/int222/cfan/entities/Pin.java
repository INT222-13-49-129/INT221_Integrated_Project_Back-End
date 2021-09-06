package sit.int222.cfan.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Pin {

    @Id
    private long pinid;
    private String email;

    @JsonIgnore
    private String pincode;
    private java.sql.Timestamp exp;

    @JsonBackReference(value = "user-pin")
    @ManyToOne
    private User user;

}
