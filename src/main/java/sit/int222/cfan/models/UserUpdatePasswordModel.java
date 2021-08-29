package sit.int222.cfan.models;

import lombok.Data;

@Data
public class UserUpdatePasswordModel {
    private long userid;
    private String oldpassword;
    private String newpassword;
}
