package sit.int222.cfan.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Jwtblacklist {
    @Id
    private long jwtblacklistid;
    private String token;
    private java.sql.Timestamp exp;

    @JsonBackReference(value = "user-jwt")
    @ManyToOne
    private User user;

}
