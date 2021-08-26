package sit.int222.cfan.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminApi {

    @GetMapping("")
    public String test() {
        return "admin";
    }
}
