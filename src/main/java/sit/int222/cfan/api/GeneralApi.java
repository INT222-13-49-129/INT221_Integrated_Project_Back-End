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
import sit.int222.cfan.controllers.FoodmenuController;
import sit.int222.cfan.controllers.FoodtypeController;
import sit.int222.cfan.controllers.IngredientsController;
import sit.int222.cfan.controllers.UserController;
import sit.int222.cfan.entities.Foodmenu;
import sit.int222.cfan.entities.Foodtype;
import sit.int222.cfan.entities.Ingredients;
import sit.int222.cfan.entities.IngredientsType;
import sit.int222.cfan.models.LoginModel;
import sit.int222.cfan.models.LoginResponseModel;
import sit.int222.cfan.models.PinModel;
import sit.int222.cfan.models.RegisterModel;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/general")
public class GeneralApi {
    @Autowired
    private FoodmenuController foodmenuController;
    @Autowired
    private IngredientsController ingredientsController;
    @Autowired
    private FoodtypeController foodtypeController;
    @Autowired
    private UserController userController;

    @GetMapping("/foodmenu")
    public List<Foodmenu> foodmenus() {
        return foodmenuController.findPUBLISH();
    }

    @GetMapping("/foodmenu/page")
    public Page<Foodmenu> foodmenusWithPage(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "foodmenuid") String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return foodmenuController.findPagePUBLISH(pageable);
    }

    @GetMapping("/foodmenu/page/search")
    public Page<Foodmenu> foodmenusWithPageSearch(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "foodmenuid") String sortBy,
            @RequestParam(defaultValue = "") String searchData) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return foodmenuController.findPageSearchPUBLISH(searchData, pageable);
    }

    @GetMapping("/foodmenu/page/foodtype")
    public Page<Foodmenu> foodmenusWithPageFoodtype(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "foodmenuid") String sortBy,
            @RequestParam(defaultValue = "0") Long foodtypeId) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return foodmenuController.findPageFoodtypePUBLISH(foodtypeId, pageable);
    }

    @GetMapping("/foodmenu/{id}")
    public Foodmenu foodmenu(@PathVariable Long id) {
        return foodmenuController.findByIdPUBLISH(id);
    }

    @GetMapping(value = "/foodmenu/img/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public Resource foodmenuImg(@PathVariable Long id) {
        return foodmenuController.getfoodmenuImgPUBLISH(id);
    }

    @GetMapping("/ingredients")
    public List<Ingredients> ingredients() {
        return ingredientsController.findAll();
    }

    @GetMapping("/ingredients/page")
    public Page<Ingredients> ingredientsWithPage(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "ingredientsid") String sortBy,
            @RequestParam(defaultValue = "") IngredientsType type,
            @RequestParam(defaultValue = "") String searchData) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return ingredientsController.findPage(type, searchData, pageable);
    }

    @GetMapping("/ingredients/type")
    public ResponseEntity<IngredientsType[]> ingredientsType() {
        return ResponseEntity.ok(ingredientsController.ingredientsType());
    }

    @GetMapping("/ingredients/{id}")
    public Ingredients ingredientsId(@PathVariable Long id) {
        return ingredientsController.findById(id);
    }

    @GetMapping("/foodtype")
    public List<Foodtype> foodtypes() {
        return foodtypeController.findAll();
    }

    @GetMapping("/foodtype/{id}")
    public Foodtype foodtypeId(@PathVariable Long id) {
        return foodtypeController.findById(id);
    }

    @PostMapping("/register")
    public ResponseEntity<Map> register(@RequestPart RegisterModel regis) {
        return ResponseEntity.ok(userController.register(regis));
    }

    @PostMapping("/pin/verify")
    public LoginResponseModel pinverify(@RequestPart PinModel emailpin) {
        return userController.verifypin(emailpin);
    }

    @PostMapping("/pin/resend")
    public ResponseEntity<Map> pinresend(@RequestPart PinModel email) {
        return ResponseEntity.ok(userController.pinresend(email));
    }

    @PostMapping("/pin/forgotpass")
    public ResponseEntity<Map> pinforgotpass(@RequestPart PinModel email) {
        return ResponseEntity.ok(userController.pinforgotpass(email));
    }

    @PostMapping("/pin/verify/forgotpass")
    public LoginResponseModel pinverifyforgotpass(@RequestPart PinModel emailpinpass) {
        return userController.verifypinforgotpass(emailpinpass);
    }

    @PostMapping("/login")
    public LoginResponseModel login(@RequestPart LoginModel login) {
        return userController.login(login);
    }

}
