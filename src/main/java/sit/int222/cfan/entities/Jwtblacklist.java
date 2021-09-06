package sit.int222.cfan.entities;

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

    @ManyToOne
    private User user;

}
