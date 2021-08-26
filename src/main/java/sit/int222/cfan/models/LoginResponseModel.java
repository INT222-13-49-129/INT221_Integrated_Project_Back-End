package sit.int222.cfan.models;

import lombok.Data;
import sit.int222.cfan.entities.User;

@Data
public class LoginResponseModel {
    private User user;
    private boolean success;
    private  String token;

    public LoginResponseModel(User user, boolean success, String token) {
        this.user = user;
        this.success = success;
        this.token = token;
    }
}
