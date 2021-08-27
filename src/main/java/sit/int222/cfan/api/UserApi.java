package sit.int222.cfan.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.int222.cfan.controllers.UserController;
import sit.int222.cfan.entities.User;

@RestController
@RequestMapping("/api/user")
public class UserApi {
    @Autowired
    private UserController userController;

    @GetMapping("")
    public User user() {
        return userController.getUser();
    }
}
