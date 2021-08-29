package sit.int222.cfan.models;

import lombok.Data;
import sit.int222.cfan.entities.User;

@Data
public class UserUpdateModel {
    private long userid;
    private String username;
    private String firstname;
    private String lastname;
    private java.sql.Date doB;
    private User.Gender gender;
    private double weight;
    private double height;
}
