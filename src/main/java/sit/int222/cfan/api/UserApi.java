package sit.int222.cfan.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sit.int222.cfan.controllers.FoodmenuController;
import sit.int222.cfan.controllers.MealController;
import sit.int222.cfan.controllers.RequestController;
import sit.int222.cfan.controllers.UserController;
import sit.int222.cfan.entities.Foodmenu;
import sit.int222.cfan.entities.Meal;
import sit.int222.cfan.entities.Request;
import sit.int222.cfan.entities.User;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;
import sit.int222.cfan.models.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserApi {
    @Autowired
    private UserController userController;
    @Autowired
    private FoodmenuController foodmenuController;
    @Autowired
    private MealController mealController;
    @Autowired
    private RequestController requestController;

    @GetMapping("")
    public User user() {
        return userController.getUser();
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Map<String, Boolean>> logout() {
        return ResponseEntity.ok(userController.logout());
    }

    @PostMapping(value = "/addimgprofile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, Object>> addImgProfile(@RequestParam(value = "file", required = false) MultipartFile fileImg) {
        if (fileImg == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.FILE_SUBMITTED_NOT_FOUND, "File : submitted file was not found");
        }
        return ResponseEntity.ok(userController.addImgProfile(fileImg));
    }

    @GetMapping(value = "/imgprofile", produces = MediaType.IMAGE_PNG_VALUE)
    public Resource getImgProfile() {
        return userController.getImgProfile(userController.getUser());
    }

    @PutMapping("/update")
    public User updateUser(@RequestPart UserUpdateModel userupdate) {
        return userController.updateUser(userController.getUser(), userupdate);
    }

    @PutMapping("/update/psw")
    public ResponseEntity<Map<String, Boolean>> updateUserPassword(@RequestPart UserUpdatePasswordModel userpsw) {
        return ResponseEntity.ok(userController.updateUserPassword(userpsw));
    }

    @PutMapping("/update/email")
    public ResponseEntity<Map<String, Object>> updateUserEmail(@RequestPart UserUpdateEmailModel useremail) {
        return ResponseEntity.ok(userController.updateUserEmail(useremail));
    }

    @PostMapping("/update/email/verify")
    public User pinverify(@RequestPart PinModel emailpin) {
        return userController.verifypinEmail(emailpin);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@RequestPart DeleteUserModel userdelete) {
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
            @RequestParam(defaultValue = "foodmenuid") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction,sortBy));
        return foodmenuController.findPageUser(userController.getUser(), pageable);
    }

    @GetMapping("/foodmenu/page/search")
    public Page<Foodmenu> foodmenusWithPageSearch(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "foodmenuid") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(defaultValue = "") String searchData) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction,sortBy));
        return foodmenuController.findPageSearchUser(userController.getUser(), searchData, pageable);
    }

    @GetMapping("/foodmenu/page/foodtype")
    public Page<Foodmenu> foodmenusWithPageFoodtype(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "foodmenuid") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(defaultValue = "0") Long foodtypeId) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction,sortBy));
        return foodmenuController.findPageFoodtypeUser(userController.getUser(), foodtypeId, pageable);
    }

    @GetMapping("/foodmenu/page/search/foodtype")
    public Page<Foodmenu> foodmenusWithPageSearchFoodtype(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "foodmenuid") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(defaultValue = "") String searchData,
            @RequestParam(defaultValue = "0") Long foodtypeId) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction,sortBy));
        return foodmenuController.findPageSearchFoodtypeUser(userController.getUser(), searchData, foodtypeId, pageable);
    }

    @GetMapping("/foodmenu/{id}")
    public Foodmenu foodmenu(@PathVariable Long id) {
        return foodmenuController.findByIdUser(userController.getUser(), id);
    }

    @PostMapping(value = "/foodmenu/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Foodmenu createFoodmenu(@RequestParam(value = "file", required = false) MultipartFile fileImg, @RequestPart Foodmenu newfoodmenu) {
        if (fileImg == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.FILE_SUBMITTED_NOT_FOUND, "File : submitted file was not found");
        }
        return foodmenuController.createFoodmenu(userController.getUser(), fileImg, newfoodmenu);
    }

    @PutMapping(value = "/foodmenu/edit/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Foodmenu updateFoodmenu(@RequestParam(value = "file", required = false) MultipartFile fileImg, @RequestPart Foodmenu updatefoodmenu, @PathVariable Long id) {
        return foodmenuController.updateFoodmenuUser(userController.getUser(), fileImg, updatefoodmenu, id);
    }

    @DeleteMapping("/foodmenu/delete/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteFoodmenu(@PathVariable Long id) {
        return ResponseEntity.ok(foodmenuController.deleteFoodmenuUser(userController.getUser(), id));
    }

    @GetMapping(value = "/foodmenu/img/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public Resource foodmenuImg(@PathVariable Long id) {
        return foodmenuController.getfoodmenuImgUser(userController.getUser(), id);
    }

    @GetMapping("/meal")
    public List<Meal> meals() {
        return mealController.findMealsUser(userController.getUser());
    }

    @GetMapping("/meal/{id}")
    public Meal meal(@PathVariable Long id) {
        return mealController.findByIdUser(userController.getUser(), id);
    }

    @GetMapping("/meal/date/{date}")
    public List<Meal> mealDate(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        System.out.println(date);
        return mealController.findByDateUser(userController.getUser(), Date.valueOf(date));
    }

    @PostMapping(value = "/meal/add")
    public Meal createMeal(@RequestPart Meal newmeal) {
        return mealController.createMeal(userController.getUser(), newmeal);
    }

    @PutMapping(value = "/meal/edit/{id}")
    public Meal updateMeal(@RequestPart Meal updatemeal, @PathVariable Long id) {
        return mealController.updateMeal(userController.getUser(), updatemeal, id);
    }

    @DeleteMapping("/meal/delete/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteMeal(@PathVariable Long id) {
        return ResponseEntity.ok(mealController.deleteMeal(userController.getUser(), id));
    }

    @GetMapping("/request")
    public List<Request> requests() {
        return requestController.findRequestsUser(userController.getUser());
    }

    @GetMapping("/request/page")
    public Page<Request> requestsWithPage(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "requestid") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction,sortBy));
        return requestController.findPageRequestsUser(userController.getUser(),pageable);
    }

    @GetMapping("/request/{id}")
    public Request request(@PathVariable Long id) {
        return requestController.findByIdUser(userController.getUser(), id);
    }

    @PostMapping(value = "/request/add")
    public Request createRequest(@RequestPart Request newrequest) {
        return requestController.createRequest(userController.getUser(), newrequest);
    }

    @DeleteMapping("/request/delete/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteRequest(@PathVariable Long id) {
        return ResponseEntity.ok(requestController.deleteRequestUser(userController.getUser(), id));
    }
}
