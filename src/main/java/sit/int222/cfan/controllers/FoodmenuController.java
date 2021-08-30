package sit.int222.cfan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import sit.int222.cfan.entities.Foodmenu;
import sit.int222.cfan.entities.FoodmenuHasIngredians;
import sit.int222.cfan.entities.User;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;
import sit.int222.cfan.repositories.FoodmenuHasIngrediansRepository;
import sit.int222.cfan.repositories.FoodmenuRepository;
import sit.int222.cfan.repositories.IngrediansRepository;
import sit.int222.cfan.services.StorageService;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Service
public class FoodmenuController {
    @Autowired
    private FoodmenuRepository foodmenuRepository;
    @Autowired
    private FoodmenuHasIngrediansRepository foodmenuHasIngrediansRepository;
    @Autowired
    private IngrediansRepository ingrediansRepository;
    @Autowired
    StorageService storageService;

    public List<Foodmenu> findAll() {
        return foodmenuRepository.findAll();
    }

    public List<Foodmenu> findPUBLISH() {
        return foodmenuRepository.findAllByFoodmenustatus(Foodmenu.FoodmenuStatus.PUBLISH);
    }

    public Page<Foodmenu> findPagePUBLISH(Pageable pageable) {
        return foodmenuRepository.findAllByFoodmenustatus(Foodmenu.FoodmenuStatus.PUBLISH, pageable);
    }

    public Page<Foodmenu> findPageSearchPUBLISH(String search, Pageable pageable) {
        return foodmenuRepository.findSearch(Foodmenu.FoodmenuStatus.PUBLISH, search, pageable);
    }

    public Page<Foodmenu> findPageFoodtypePUBLISH(long foodtypeId, Pageable pageable) {
        return foodmenuRepository.findAllByFoodtypeId(Foodmenu.FoodmenuStatus.PUBLISH, foodtypeId, pageable);
    }

    public Foodmenu findByIdPUBLISH(long id) {
        Foodmenu foodmenu = foodmenuRepository.findByFoodmenuidAndFoodmenustatus(id, Foodmenu.FoodmenuStatus.PUBLISH);
        if (foodmenu == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.FOODMENU_DOES_NOT_EXIST, "Foodmenu : id {" + id + "} does not exist !!");
        }
        return foodmenu;
    }

    public List<Foodmenu> findFoodmenusUser(User user) {
        return foodmenuRepository.findAllByUser(user);
    }

    public Page<Foodmenu> findPageUser(User user, Pageable pageable) {
        return foodmenuRepository.findAllByUser(user, pageable);
    }

    public Page<Foodmenu> findPageSearchUser(User user, String search, Pageable pageable) {
        return foodmenuRepository.findAllByFoodnameContainingOrDescriptionContainingAndUser(user, search, pageable);
    }

    public Page<Foodmenu> findPageFoodtypeUser(User user, long foodtypeId, Pageable pageable) {
        return foodmenuRepository.findAllByFoodtypeIdUser(user, foodtypeId, pageable);
    }

    public Foodmenu findByIdUser(User user, long id) {
        Foodmenu foodmenu = foodmenuRepository.findByUserAndFoodmenuid(user, id);
        if (foodmenu == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.FOODMENU_DOES_NOT_EXIST, "Foodmenu : id {" + id + "} does not exist !!");
        }
        return foodmenu;
    }

    public Foodmenu createFoodmenu(User user, MultipartFile fileImg, @RequestPart Foodmenu newfoodmenu) {
        if (newfoodmenu.getFoodmenustatus().equals(Foodmenu.FoodmenuStatus.PUBLISH)) {
            if (foodmenuRepository.findByFoodnameAndFoodmenustatus(newfoodmenu.getFoodname(), Foodmenu.FoodmenuStatus.PUBLISH) != null) {
                throw new BaseException(ExceptionResponse.ERROR_CODE.FOODMENU_FOODNAME_PUBLISH_ALREADY_EXIST, "Foodmenu :Foodname {" + newfoodmenu.getFoodname() + "} does already exist !!");
            }
        } else {
            if (foodmenuRepository.findByUserAndFoodname(user, newfoodmenu.getFoodname()) != null) {
                throw new BaseException(ExceptionResponse.ERROR_CODE.FOODMENU_FOODNAME_PERSONAL_ALREADY_EXIST, "Foodmenu :Foodname {" + newfoodmenu.getFoodname() + "} does already exist !!");
            }
        }
        Foodmenu foodmenu = new Foodmenu();
        foodmenu.setFoodname(newfoodmenu.getFoodname());
        foodmenu.setFoodmenustatus(newfoodmenu.getFoodmenustatus());
        foodmenu.setFoodtype(newfoodmenu.getFoodtype());
        foodmenu = foodmenuRepository.save(foodmenu);
        try {
        List<FoodmenuHasIngredians> listtotalkcal = calculatetotalkcalIngredians(newfoodmenu.getFoodmenuHasIngrediansList(),foodmenu);
        foodmenu.setFoodmenuHasIngrediansList(listtotalkcal);
        foodmenu.setTotalkcal(calculatetotalkcal(listtotalkcal));
        foodmenu.setDescription(newfoodmenu.getDescription());
        foodmenu.setUser(user);
        String s = "FM-";
            foodmenu.setImage(storageService.store(fileImg, s.concat(String.valueOf(foodmenu.getFoodmenuid()))));
        } catch (Exception e) {
            foodmenuRepository.delete(foodmenu);
            throw new BaseException(ExceptionResponse.ERROR_CODE.FILE_CAN_NOT_SAVE,"File : file cannot be saved !!");
        }
        return foodmenuRepository.save(foodmenu);
    }

    public List<FoodmenuHasIngredians> calculatetotalkcalIngredians(List<FoodmenuHasIngredians> foodmenuHasIngrediansList,Foodmenu foodmenu) {
        List<FoodmenuHasIngredians> list = new ArrayList<>();
        ListIterator<FoodmenuHasIngredians> iterator = foodmenuHasIngrediansList.listIterator();
        while (iterator.hasNext()) {
            FoodmenuHasIngredians foodmenuHasIngredians = iterator.next();
            foodmenuHasIngredians.setFoodmenu(foodmenu);
            foodmenuHasIngredians.setIngredians(ingrediansRepository.getById(foodmenuHasIngredians.getKey().getIngrediansIngradiansid()));
            foodmenuHasIngredians.getKey().setFoodmenuFoodmenuid(foodmenu.getFoodmenuid());
            long totalkcal = foodmenuHasIngredians.getIngredians().getKcalpunit() * foodmenuHasIngredians.getTotalunit();
            foodmenuHasIngredians.setTotalkcal(totalkcal);
            list.add(foodmenuHasIngrediansRepository.save(foodmenuHasIngredians));
        }
        return list;
    }

    public long calculatetotalkcal(List<FoodmenuHasIngredians> foodmenuHasIngrediansList){
        long totalkcal = 0;
        ListIterator<FoodmenuHasIngredians> iterator = foodmenuHasIngrediansList.listIterator();
        while (iterator.hasNext()) {
            FoodmenuHasIngredians foodmenuHasIngredians = iterator.next();
            totalkcal += foodmenuHasIngredians.getTotalkcal();
        }
        return totalkcal;
    }
}
