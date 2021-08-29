package sit.int222.cfan.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sit.int222.cfan.controllers.FoodmenuController;
import sit.int222.cfan.controllers.UserController;
import sit.int222.cfan.entities.Foodmenu;
import sit.int222.cfan.entities.User;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;
import sit.int222.cfan.models.DeleteUserModel;
import sit.int222.cfan.models.UserUpdateEmailModel;
import sit.int222.cfan.models.UserUpdateModel;
import sit.int222.cfan.models.UserUpdatePasswordModel;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserApi {
    @Autowired
    private UserController userController;
    @Autowired
    private FoodmenuController foodmenuController;

    @GetMapping("")
    public User user() {
        return userController.getUser();
    }

    @PostMapping("/logout")
    public ResponseEntity<Map> logout(){
        return ResponseEntity.ok(userController.logout());
    }

    @PostMapping(value = "/addimgprofile",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map> addImgProfile(@RequestParam(value = "file",required = false) MultipartFile fileImg) {
        if(fileImg == null){
            throw new BaseException(ExceptionResponse.ERROR_CODE.FILE_SUBMITTED_NOT_FOUND,"File : submitted file was not found");
        }
       return ResponseEntity.ok(userController.addImgProfile(fileImg));
    }

    @GetMapping(value = "/imgprofile",produces = MediaType.IMAGE_PNG_VALUE)
    public Resource getImgProfile() throws IOException, URISyntaxException {
        return userController.getImgProfile();
    }

    @PutMapping("/update")
    public User updateUser(@RequestPart UserUpdateModel userupdate) {
        return userController.updateUser(userupdate);
    }

    @PutMapping("/update/psw")
    public ResponseEntity<Map> updateUserPassword(@RequestPart UserUpdatePasswordModel userpsw) {
        return ResponseEntity.ok(userController.updateUserPassword(userpsw));
    }

    @PutMapping("/update/email")
    public ResponseEntity<Map> updateUserEmail(@RequestPart UserUpdateEmailModel useremail) {
        return ResponseEntity.ok(userController.updateUserEmail(useremail));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map> deleteUser(@RequestPart DeleteUserModel userdelete) {
        return ResponseEntity.ok(userController.deleteUser(userdelete));
    }

    @GetMapping("/foodmenu")
    public List<Foodmenu> foodmenus() {
        return foodmenuController.findFoodmenusUser(userController.getUser());
    }

    @GetMapping("/foodmenu/page")
    public Page<Foodmenu> foodmenusWithPage(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "foodmenuid") String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return foodmenuController.findPageUser(userController.getUser(),pageable);
    }

    @GetMapping("/foodmenu/page/search")
    public Page<Foodmenu> foodmenusWithPageSearch(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "foodmenuid") String sortBy,
            @RequestParam(defaultValue = "") String searchData) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return foodmenuController.findPageSearchUser(userController.getUser(),searchData, pageable);
    }

    @GetMapping("/foodmenu/page/foodtype")
    public Page<Foodmenu> foodmenusWithPageFoodtype(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "foodmenuid") String sortBy,
            @RequestParam(defaultValue = "0") Long foodtypeId) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return foodmenuController.findPageFoodtypeUser(userController.getUser(),foodtypeId, pageable);
    }

    @GetMapping("/foodmenu/{id}")
    public Foodmenu product(@PathVariable Long id) {
        return foodmenuController.findByIdUser(userController.getUser(),id);
    }
}
