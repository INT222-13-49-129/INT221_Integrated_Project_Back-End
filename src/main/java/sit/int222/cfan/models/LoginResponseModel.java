package sit.int222.cfan.models;

import lombok.Data;
import sit.int222.cfan.entities.User;

@Data
public class LoginResponseModel {
    private User user;
    private boolean success;

    public LoginResponseModel(boolean success,User user) {
        this.success = success;
        this.user = user;
    }
}
