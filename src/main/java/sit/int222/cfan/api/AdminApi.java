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
import sit.int222.cfan.controllers.*;
import sit.int222.cfan.entities.*;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminApi {
    @Autowired
    private UserController userController;
    @Autowired
    private FoodmenuController foodmenuController;
    @Autowired
    private IngredientsController ingredientsController;
    @Autowired
    private FoodtypeController foodtypeController;
    @Autowired
    private RequestController requestController;

    @PutMapping("/changestatus")
    public User changestatus() {
        User user = userController.changestatus(userController.getUser(), User.Status.NORMAL);
        if (user.getStatus().equals(User.Status.NORMAL)) {
            userController.logout();
        }
        return user;
    }

    @GetMapping("/user")
    public List<User> users() {
        return userController.getUserAll();
    }

    @GetMapping("/user/page")
    public Page<User> usersWithPage(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "userid") String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return userController.getUserPage(pageable);
    }

    @GetMapping("/user/{id}")
    public User user(@PathVariable Long id) {
        return userController.getUserById(id);
    }

    @GetMapping("/user/{id}/foodmenu")
    public Page<Foodmenu> foodmenuuser(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "foodmenuid") String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return foodmenuController.findPageUser(userController.getUserById(id), pageable);
    }

    @GetMapping(value = "/user/{id}/imgprofile", produces = MediaType.IMAGE_PNG_VALUE)
    public Resource userImgProfile(@PathVariable Long id) {
        return userController.getImgProfile(userController.getUserById(id));
    }

    @PutMapping("/user/{id}/changestatus")
    public User changestatus(@PathVariable Long id) {
        return userController.changestatus(userController.getUserById(id), User.Status.ADMIN);
    }

    @GetMapping("/foodmenu")
    public List<Foodmenu> foodmenus() {
        return foodmenuController.findAll();
    }

    @GetMapping("/foodmenu/page")
    public Page<Foodmenu> foodmenusWithPage(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "foodmenuid") String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return foodmenuController.findPageAll(pageable);
    }

    @GetMapping("/foodmenu/{id}")
    public Foodmenu foodmenusWithPage(@PathVariable Long id) {
        return foodmenuController.findById(id);
    }

    @GetMapping(value = "/foodmenu/img/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public Resource foodmenuImg(@PathVariable Long id) {
        return foodmenuController.getfoodmenuImgId(id);
    }

    @PostMapping(value = "/foodmenu/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Foodmenu createFoodmenu(@RequestParam(value = "file", required = false) MultipartFile fileImg, @RequestPart Foodmenu newfoodmenu) {
        if (fileImg == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.FILE_SUBMITTED_NOT_FOUND, "File : submitted file was not found");
        }
        return foodmenuController.createFoodmenu(null, fileImg, newfoodmenu);
    }

    @PutMapping(value = "/foodmenu/edit/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Foodmenu updateFoodmenu(@RequestParam(value = "file", required = false) MultipartFile fileImg, @RequestPart Foodmenu updatefoodmenu, @PathVariable Long id) {
        return foodmenuController.updateFoodmenuId(fileImg, updatefoodmenu, id);
    }

    @DeleteMapping("/foodmenu/delete/{id}")
    public ResponseEntity<Map> deleteFoodmenu(@PathVariable Long id) {
        return ResponseEntity.ok(foodmenuController.deleteFoodmenuId(id));
    }

    @PostMapping("/ingredients/add")
    public Ingredients createIngredients(@RequestPart Ingredients newingredients) {
        return ingredientsController.createIngredients(newingredients);
    }

    @PutMapping(value = "/ingredients/edit/{id}")
    public Ingredients updateIngredients(@RequestPart Ingredients updateingredients, @PathVariable Long id) {
        return ingredientsController.updateIngredients(updateingredients, id);
    }

    @DeleteMapping("/ingredients/delete/{id}")
    public ResponseEntity<Map> deleteIngredients(@PathVariable Long id) {
        return ResponseEntity.ok(ingredientsController.deleteIngredients(id));
    }

    @PostMapping("/foodtype/add")
    public Foodtype createFoodtype(@RequestPart Foodtype newfoodtype) {
        return foodtypeController.createFoodtype(newfoodtype);
    }

    @PutMapping(value = "/foodtype/edit/{id}")
    public Foodtype updateFoodtype(@RequestPart Foodtype updatefoodtype, @PathVariable Long id) {
        return foodtypeController.updateFoodtype(updatefoodtype, id);
    }

    @DeleteMapping("/foodtype/delete/{id}")
    public ResponseEntity<Map> deleteFoodtype(@PathVariable Long id) {
        return ResponseEntity.ok(foodtypeController.deleteFoodtype(id));
    }

    @GetMapping("/request")
    public List<Request> requests() {
        return requestController.findAll();
    }

    @GetMapping("/request/{id}")
    public Request request(@PathVariable Long id) {
        return requestController.findById(id);
    }

    @GetMapping("/request/status")
    public Request.Status[] requestStatus() {
        return requestController.requestStatus();
    }

    @PutMapping("/request/changestatus/{id}")
    public Request changestatusRequest(@PathVariable Long id, @RequestPart Request request) {
        return requestController.changestatus(id, request);
    }

    @DeleteMapping("/request/delete/{id}")
    public ResponseEntity<Map> deleteRequest(@PathVariable Long id) {
        return ResponseEntity.ok(requestController.deleteRequestId(id));
    }
}
