package sit.int222.cfan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sit.int222.cfan.entities.Foodmenu;
import sit.int222.cfan.entities.FoodmenuHasIngredients;
import sit.int222.cfan.entities.User;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;
import sit.int222.cfan.repositories.FoodmenuHasIngredientsRepository;
import sit.int222.cfan.repositories.FoodmenuRepository;
import sit.int222.cfan.repositories.IngredientsRepository;
import sit.int222.cfan.services.StorageService;

import java.io.IOException;
import java.util.*;

@Service
public class FoodmenuController {
    @Autowired
    private FoodmenuRepository foodmenuRepository;
    @Autowired
    private FoodmenuHasIngredientsRepository foodmenuHasIngredientsRepository;
    @Autowired
    private IngredientsRepository ingredientsRepository;
    @Autowired
    StorageService storageService;

    public List<Foodmenu> findAll() {
        return foodmenuRepository.findAll();
    }

    public Page<Foodmenu> findPageAll(Pageable pageable) {
        return foodmenuRepository.findAll(pageable);
    }

    public Foodmenu findById(Long id) {
        Foodmenu foodmenu = foodmenuRepository.findById(id).orElse(null);
        if (foodmenu == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.FOODMENU_DOES_NOT_EXIST, "Foodmenu : id {" + id + "} does not exist !!");
        }
        return foodmenu;
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

    public Page<Foodmenu> findPageFoodtypePUBLISH(Long foodtypeId, Pageable pageable) {
        return foodmenuRepository.findAllByFoodtypeId(Foodmenu.FoodmenuStatus.PUBLISH, foodtypeId, pageable);
    }

    public Foodmenu findByIdPUBLISH(Long id) {
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

    public Page<Foodmenu> findPageFoodtypeUser(User user, Long foodtypeId, Pageable pageable) {
        return foodmenuRepository.findAllByFoodtypeIdUser(user, foodtypeId, pageable);
    }

    public Foodmenu findByIdUser(User user, Long id) {
        Foodmenu foodmenu = foodmenuRepository.findByUserAndFoodmenuid(user, id);
        if (foodmenu == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.FOODMENU_DOES_NOT_EXIST, "Foodmenu : id {" + id + "} does not exist !!");
        }
        return foodmenu;
    }

    public Foodmenu findByIdPUBLISHorUser(User user, Long id) {
        Foodmenu foodmenu = foodmenuRepository.findByUserAndFoodmenuid(user, id);
        if (foodmenu == null) {
            foodmenu = foodmenuRepository.findByFoodmenuidAndFoodmenustatus(id, Foodmenu.FoodmenuStatus.PUBLISH);
            if (foodmenu == null) {
                throw new BaseException(ExceptionResponse.ERROR_CODE.FOODMENU_DOES_NOT_EXIST, "Foodmenu : id {" + id + "} does not exist !!");
            }
        }
        return foodmenu;
    }

    public Foodmenu createFoodmenu(User user, MultipartFile fileImg, Foodmenu newfoodmenu) {
        if (newfoodmenu.getFoodmenustatus().equals(Foodmenu.FoodmenuStatus.PUBLISH)) {
            if (foodmenuRepository.findByFoodnameAndFoodmenustatus(newfoodmenu.getFoodname(), Foodmenu.FoodmenuStatus.PUBLISH) != null) {
                throw new BaseException(ExceptionResponse.ERROR_CODE.FOODMENU_FOODNAME_PUBLISH_ALREADY_EXIST, "Foodmenu :Foodname {" + newfoodmenu.getFoodname() + "} does already exist !!");
            }
        } else {
            if (user == null) {
                throw new BaseException(ExceptionResponse.ERROR_CODE.USER_IS_NULL, "User :User cannot be null if this is a personal!!");
            }
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
            List<FoodmenuHasIngredients> listtotalkcal = calculatetotalkcalIngredients(newfoodmenu.getFoodmenuHasIngredientsList(), foodmenu);
            foodmenu.setFoodmenuHasIngredientsList(listtotalkcal);
            foodmenu.setTotalkcal(calculatetotalkcal(listtotalkcal));
            foodmenu.setDescription(newfoodmenu.getDescription());
            foodmenu.setUser(user);
        } catch (Exception e) {
            foodmenuRepository.delete(foodmenu);
            throw e;
        }
        try {
            String s = "FM-";
            foodmenu.setImage(storageService.store(fileImg, s.concat(String.valueOf(foodmenu.getFoodmenuid()))));
        } catch (Exception e) {
            foodmenuRepository.delete(foodmenu);
            throw new BaseException(ExceptionResponse.ERROR_CODE.FILE_CAN_NOT_SAVE, "File : file cannot be saved !!");
        }
        return foodmenuRepository.save(foodmenu);
    }

    public Foodmenu updateFoodmenu(Foodmenu foodmenu, MultipartFile fileImg, Foodmenu updatefoodmenu) {
        if (updatefoodmenu.getFoodmenustatus().equals(Foodmenu.FoodmenuStatus.PUBLISH)) {
            if (foodmenuRepository.findByFoodnameAndFoodmenustatus(updatefoodmenu.getFoodname(), Foodmenu.FoodmenuStatus.PUBLISH) != null && !updatefoodmenu.getFoodname().equals(foodmenu.getFoodname())) {
                throw new BaseException(ExceptionResponse.ERROR_CODE.FOODMENU_FOODNAME_PUBLISH_ALREADY_EXIST, "Foodmenu :Foodname {" + updatefoodmenu.getFoodname() + "} does already exist !!");
            }
        } else {
            if (foodmenu.getUser() == null) {
                throw new BaseException(ExceptionResponse.ERROR_CODE.USER_IS_NULL, "User :User cannot be null if this is a personal!!");
            }
            if (foodmenuRepository.findByUserAndFoodname(foodmenu.getUser(), updatefoodmenu.getFoodname()) != null && !updatefoodmenu.getFoodname().equals(foodmenu.getFoodname())) {
                throw new BaseException(ExceptionResponse.ERROR_CODE.FOODMENU_FOODNAME_PERSONAL_ALREADY_EXIST, "Foodmenu :Foodname {" + updatefoodmenu.getFoodname() + "} does already exist !!");
            }
        }
        foodmenu.setFoodname(updatefoodmenu.getFoodname());
        foodmenu.setFoodmenustatus(updatefoodmenu.getFoodmenustatus());
        foodmenu.setFoodtype(updatefoodmenu.getFoodtype());
        foodmenuHasIngredientsRepository.deleteAll(foodmenu.getFoodmenuHasIngredientsList());
        List<FoodmenuHasIngredients> listtotalkcal = calculatetotalkcalIngredients(updatefoodmenu.getFoodmenuHasIngredientsList(), foodmenu);
        foodmenu.setFoodmenuHasIngredientsList(listtotalkcal);
        foodmenu.setTotalkcal(calculatetotalkcal(listtotalkcal));
        foodmenu.setDescription(updatefoodmenu.getDescription());
        if (fileImg != null) {
            String s = "FM-";
            try {
                storageService.delete(foodmenu.getImage());
                foodmenu.setImage(storageService.store(fileImg, s.concat(String.valueOf(foodmenu.getFoodmenuid()))));
            } catch (Exception e) {
                throw new BaseException(ExceptionResponse.ERROR_CODE.FILE_CAN_NOT_SAVE, "File : file cannot be saved !!");
            }
        }
        return foodmenuRepository.save(foodmenu);
    }

    public Foodmenu updateFoodmenuUser(User user, MultipartFile fileImg, Foodmenu updatefoodmenu, Long id) {
        Foodmenu foodmenu = findByIdUser(user, id);
        return updateFoodmenu(foodmenu, fileImg, updatefoodmenu);
    }

    public Foodmenu updateFoodmenuId(MultipartFile fileImg, Foodmenu updatefoodmenu, Long id) {
        Foodmenu foodmenu = findById(id);
        return updateFoodmenu(foodmenu, fileImg, updatefoodmenu);
    }

    public Map<String, Boolean> deleteFoodmenu(Foodmenu foodmenu) {
        try {
            foodmenuHasIngredientsRepository.deleteAll(foodmenu.getFoodmenuHasIngredientsList());
            storageService.delete(foodmenu.getImage());
            foodmenuRepository.delete(foodmenu);
        } catch (IOException e) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.FILE_CAN_NOT_DELETE, "File : file cannot delete !!");
        }
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return map;
    }

    public Map<String, Boolean> deleteFoodmenuUser(User user, Long id) {
        Foodmenu foodmenu = findByIdUser(user, id);
        return deleteFoodmenu(foodmenu);
    }

    public Map<String, Boolean> deleteFoodmenuId(Long id) {
        Foodmenu foodmenu = findById(id);
        return deleteFoodmenu(foodmenu);
    }

    public List<FoodmenuHasIngredients> calculatetotalkcalIngredients(List<FoodmenuHasIngredients> foodmenuHasIngredientsList, Foodmenu foodmenu) {
        List<FoodmenuHasIngredients> list = new ArrayList<>();
        ListIterator<FoodmenuHasIngredients> iterator = foodmenuHasIngredientsList.listIterator();
        while (iterator.hasNext()) {
            FoodmenuHasIngredients foodmenuHasIngredients = iterator.next();
            foodmenuHasIngredients.setFoodmenu(foodmenu);
            foodmenuHasIngredients.setIngredients(ingredientsRepository.getById(foodmenuHasIngredients.getKey().getIngredientsIngredientsid()));
            foodmenuHasIngredients.getKey().setFoodmenuFoodmenuid(foodmenu.getFoodmenuid());
            Long totalkcal = foodmenuHasIngredients.getIngredients().getKcalpunit() * foodmenuHasIngredients.getTotalunit();
            foodmenuHasIngredients.setTotalkcal(totalkcal);
            list.add(foodmenuHasIngredientsRepository.save(foodmenuHasIngredients));
        }
        return list;
    }

    public Long calculatetotalkcal(List<FoodmenuHasIngredients> foodmenuHasIngredientsList) {
        Long totalkcal = 0L;
        ListIterator<FoodmenuHasIngredients> iterator = foodmenuHasIngredientsList.listIterator();
        while (iterator.hasNext()) {
            FoodmenuHasIngredients foodmenuHasIngredients = iterator.next();
            totalkcal += foodmenuHasIngredients.getTotalkcal();
        }
        return totalkcal;
    }

    public Resource getfoodmenuImg(Foodmenu foodmenu) {
        try {
            return storageService.loadAsResource(foodmenu.getImage());
        } catch (Exception e) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.FILE_NOT_FOUND, "File : name {" + foodmenu.getImage() + "} not found !!");
        }
    }

    public Resource getfoodmenuImgId(Long id) {
        Foodmenu foodmenu = findById(id);
        return getfoodmenuImg(foodmenu);
    }

    public Resource getfoodmenuImgPUBLISH(Long id) {
        Foodmenu foodmenu = findByIdPUBLISH(id);
        return getfoodmenuImg(foodmenu);
    }

    public Resource getfoodmenuImgUser(User user, Long id) {
        Foodmenu foodmenu = findByIdUser(user, id);
        return getfoodmenuImg(foodmenu);
    }
}
