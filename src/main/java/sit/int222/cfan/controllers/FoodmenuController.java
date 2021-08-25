package sit.int222.cfan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sit.int222.cfan.entities.Foodmenu;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;
import sit.int222.cfan.repositories.FoodmenuRepository;

import java.util.List;

@Service
public class FoodmenuController {
    @Autowired
    private FoodmenuRepository foodmenuRepository;

    public List<Foodmenu>  findAll(){
        return foodmenuRepository.findAll();
    }

    public List<Foodmenu>  findPUBLISH(){
        return foodmenuRepository.findAllByFoodmenustatus(Foodmenu.FoodmenuStatus.PUBLISH);
    }

    public Page<Foodmenu> findPagePUBLISH(Pageable pageable){
        return foodmenuRepository.findAllByFoodmenustatus(Foodmenu.FoodmenuStatus.PUBLISH,pageable);
    }
    public Page<Foodmenu> findPageSearchPUBLISH(String search,Pageable pageable){
        return foodmenuRepository.findSearch(Foodmenu.FoodmenuStatus.PUBLISH,search,pageable);
    }

    public Page<Foodmenu> findPageFoodtypePUBLISH(long foodtypeId,Pageable pageable){
        return foodmenuRepository.findAllByFoodtypeId(Foodmenu.FoodmenuStatus.PUBLISH,foodtypeId,pageable);
    }

    public Foodmenu findByIdPUBLISH(long id){
        Foodmenu foodmenu = foodmenuRepository.findByFoodmenuidAndFoodmenustatus(id,Foodmenu.FoodmenuStatus.PUBLISH);
        if(foodmenu == null){
            throw new BaseException(ExceptionResponse.ERROR_CODE.FOODMENU_DOES_NOT_EXIST,"Foodmenu : id {"+id+"} does not exist !!");
        }
        return foodmenu;
    }

}
