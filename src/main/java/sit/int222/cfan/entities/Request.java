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

    @JsonBackReference("user-request")
    @ManyToOne
    private User user;

    @OneToOne(mappedBy = "request", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Ingredientsreq ingredientsreq;

    @OneToOne(mappedBy = "request", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Foodtypereq foodtypereq;

    public enum Status {
        WAIT, APPROVE, DISAPPROVED
    }
}
