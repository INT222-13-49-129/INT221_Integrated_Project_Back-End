package sit.int222.cfan.models;

import lombok.Data;

@Data
public class DeleteUserModel {
    private long userid;
    private String username;
    private String email;
    private String password;
}
