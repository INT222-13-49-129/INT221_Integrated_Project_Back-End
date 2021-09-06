package sit.int222.cfan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.int222.cfan.entities.Foodmenu;
import sit.int222.cfan.entities.Foodtype;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;
import sit.int222.cfan.repositories.FoodtypeRepository;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

@Service
public class FoodtypeController {
    @Autowired
    private FoodtypeRepository foodtypeRepository;
    @Autowired
    private FoodmenuController foodmenuController;

    public List<Foodtype> findAll() {
        return foodtypeRepository.findAll();
    }

    public Foodtype findById(Long id){
        Foodtype foodtype = foodtypeRepository.findById(id).orElse(null);
        if(foodtype == null){
            throw new BaseException(ExceptionResponse.ERROR_CODE.FOODTYPE_DOES_NOT_EXIST, "Foodtype : id {" + id + "} does not exist !!");
        }
        return foodtype;
    }

    public  Foodtype createFoodtype(Foodtype newfoodtype){
        if(foodtypeRepository.existsByTypename(newfoodtype.getTypename())){
            throw new BaseException(ExceptionResponse.ERROR_CODE.FOODTYPE_TYPENAME_ALREADY_EXIST,"Foodtype : Typename {" + newfoodtype.getTypename() + "} does already exist !!");
        }
        Foodtype foodtype = new Foodtype();
        foodtype.setTypename(newfoodtype.getTypename());
        return foodtypeRepository.save(foodtype);
    }

    public Foodtype updateFoodtype(Foodtype updatefoodtype,Long id){
        Foodtype foodtype = findById(id);
        if(foodtypeRepository.existsByTypename(updatefoodtype.getTypename()) && !foodtype.getTypename().equals(updatefoodtype.getTypename())){
            throw new BaseException(ExceptionResponse.ERROR_CODE.FOODTYPE_TYPENAME_ALREADY_EXIST,"Foodtype : Typename {" + updatefoodtype.getTypename() + "} does already exist !!");
        }
        foodtype.setTypename(updatefoodtype.getTypename());
        return foodtypeRepository.save(foodtype);
    }

    public Map<String,Boolean> deleteFoodtype(Long id){
        Foodtype foodtype = findById(id);

        List<Foodmenu> foodmenuList = foodtype.getFoodmenus();
        ListIterator<Foodmenu> iterator = foodmenuList.listIterator();
        while (iterator.hasNext()){
            Foodmenu foodmenu = iterator.next();
            foodmenuController.deleteFoodmenu(foodmenu);
        }

        foodtypeRepository.delete(foodtype);
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return map;
    }
}
