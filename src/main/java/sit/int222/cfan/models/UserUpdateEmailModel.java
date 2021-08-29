package sit.int222.cfan.models;

import lombok.Data;

@Data
public class UserUpdateEmailModel {
    private long userid;
    private String email;
    private String password;
}
