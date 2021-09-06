package sit.int222.cfan.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Request {

    @Id
    @GeneratedValue
    private long requestid;

    @Column(columnDefinition = "ENUM('WAIT', 'APPROVE', 'DISAPPROVED')")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "user_userid", insertable = false, updatable = false)
    private Long userid;

    @JsonBackReference("user-request")
    @ManyToOne
    private User user;

    @OneToOne(mappedBy = "request", cascade = CascadeType.ALL,orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private Ingredientsreq ingredientsreq;

    @OneToOne(mappedBy = "request", cascade = CascadeType.ALL,orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private Foodtypereq foodtypereq;

    public enum Status {
        WAIT, APPROVE, DISAPPROVED
    }
}
