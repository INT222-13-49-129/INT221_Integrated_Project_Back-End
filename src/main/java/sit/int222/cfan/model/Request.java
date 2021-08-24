package sit.int222.cfan.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long requestid;

    @Column(columnDefinition = "ENUM('WAIT', 'APPROVE', 'DISAPPROVED')")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    private User user;

    @OneToOne(mappedBy = "request", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Ingrediansreq ingrediansreq;

    @OneToOne(mappedBy = "request", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Foodtypereq foodtypereq;

    public enum Status {
        WAIT, APPROVE, DISAPPROVED
    }
}
