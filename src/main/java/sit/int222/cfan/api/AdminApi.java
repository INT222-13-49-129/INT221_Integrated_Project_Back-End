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
        userController.isADMIN();
        User user = userController.changestatus(userController.getUser(), User.Status.NORMAL);
        if (user.getStatus().equals(User.Status.NORMAL)) {
            userController.logout();
        }
        return user;
    }

    @GetMapping("/user")
    public List<User> users() {
        userController.isADMIN();
        return userController.getUserAll();
    }

    @GetMapping("/user/page")
    public Page<User> usersWithPage(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "userid") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        userController.isADMIN();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction,sortBy));
        return userController.getUserPage(pageable);
    }

    @GetMapping("/user/{id}")
    public User user(@PathVariable Long id) {
        userController.isADMIN();
        return userController.getUserById(id);
    }

    @GetMapping("/user/{id}/foodmenu")
    public Page<Foodmenu> foodmenuuser(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "foodmenuid") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        userController.isADMIN();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction,sortBy));
        return foodmenuController.findPageUser(userController.getUserById(id), pageable);
    }

    @GetMapping(value = "/user/{id}/imgprofile", produces = MediaType.IMAGE_PNG_VALUE)
    public Resource userImgProfile(@PathVariable Long id) {
        userController.isADMIN();
        return userController.getImgProfile(userController.getUserById(id));
    }

    @PutMapping("/user/{id}/changestatus")
    public User changestatus(@PathVariable Long id) {
        userController.isADMIN();
        return userController.changestatus(userController.getUserById(id), User.Status.ADMIN);
    }

    @GetMapping("/foodmenu")
    public List<Foodmenu> foodmenus() {
        userController.isADMIN();
        return foodmenuController.findAll();
    }

    @GetMapping("/foodmenu/page")
    public Page<Foodmenu> foodmenusWithPage(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "foodmenuid") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        userController.isADMIN();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction,sortBy));
        return foodmenuController.findPageAll(pageable);
    }

    @GetMapping("/foodmenu/page/search/foodtype")
    public Page<Foodmenu> foodmenusWithPageSearchFoodtype(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "foodmenuid") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "") String searchData,
            @RequestParam(defaultValue = "0") Long foodtypeId) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction,sortBy));
        return foodmenuController.findPageSearchFoodtypeAll(searchData, foodtypeId, pageable);
    }

    @GetMapping("/foodmenu/{id}")
    public Foodmenu foodmenusWithPage(@PathVariable Long id) {
        userController.isADMIN();
        return foodmenuController.findById(id);
    }

    @GetMapping(value = "/foodmenu/img/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public Resource foodmenuImg(@PathVariable Long id) {
        userController.isADMIN();
        return foodmenuController.getfoodmenuImgId(id);
    }

    @PostMapping(value = "/foodmenu/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Foodmenu createFoodmenu(@RequestParam(value = "file", required = false) MultipartFile fileImg, @RequestPart Foodmenu newfoodmenu) {
        userController.isADMIN();
        if (fileImg == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.FILE_SUBMITTED_NOT_FOUND, "File : submitted file was not found");
        }
        return foodmenuController.createFoodmenu(null, fileImg, newfoodmenu);
    }

    @PutMapping(value = "/foodmenu/edit/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Foodmenu updateFoodmenu(@RequestParam(value = "file", required = false) MultipartFile fileImg, @RequestPart Foodmenu updatefoodmenu, @PathVariable Long id) {
        userController.isADMIN();
        return foodmenuController.updateFoodmenuId(fileImg, updatefoodmenu, id);
    }

    @DeleteMapping("/foodmenu/delete/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteFoodmenu(@PathVariable Long id) {
        userController.isADMIN();
        return ResponseEntity.ok(foodmenuController.deleteFoodmenuId(id));
    }

    @PostMapping("/ingredients/add")
    public Ingredients createIngredients(@RequestPart Ingredients newingredients) {
        userController.isADMIN();
        return ingredientsController.createIngredients(newingredients);
    }

    @PutMapping(value = "/ingredients/edit/{id}")
    public Ingredients updateIngredients(@RequestPart Ingredients updateingredients, @PathVariable Long id) {
        userController.isADMIN();
        return ingredientsController.updateIngredients(updateingredients, id);
    }

    @DeleteMapping("/ingredients/delete/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteIngredients(@PathVariable Long id) {
        userController.isADMIN();
        return ResponseEntity.ok(ingredientsController.deleteIngredients(id));
    }

    @PostMapping("/foodtype/add")
    public Foodtype createFoodtype(@RequestPart Foodtype newfoodtype) {
        userController.isADMIN();
        return foodtypeController.createFoodtype(newfoodtype);
    }

    @PutMapping(value = "/foodtype/edit/{id}")
    public Foodtype updateFoodtype(@RequestPart Foodtype updatefoodtype, @PathVariable Long id) {
        userController.isADMIN();
        return foodtypeController.updateFoodtype(updatefoodtype, id);
    }

    @DeleteMapping("/foodtype/delete/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteFoodtype(@PathVariable Long id) {
        userController.isADMIN();
        return ResponseEntity.ok(foodtypeController.deleteFoodtype(id));
    }

    @GetMapping("/request")
    public List<Request> requests() {
        userController.isADMIN();
        return requestController.findAll();
    }

    @GetMapping("/request/page")
    public Page<Request> requestsWithPage(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "requestid") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        userController.isADMIN();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction,sortBy));
        return requestController.findPageRequests(pageable);
    }

    @GetMapping("/request/{id}")
    public Request request(@PathVariable Long id) {
        userController.isADMIN();
        return requestController.findById(id);
    }

    @GetMapping("/request/status")
    public Request.Status[] requestStatus() {
        userController.isADMIN();
        return requestController.requestStatus();
    }

    @PutMapping("/request/changestatus/{id}")
    public Request changestatusRequest(@PathVariable Long id, @RequestPart Request request) {
        userController.isADMIN();
        return requestController.changestatus(id, request);
    }

    @DeleteMapping("/request/delete/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteRequest(@PathVariable Long id) {
        userController.isADMIN();
        return ResponseEntity.ok(requestController.deleteRequestId(id));
    }
}
