package sit.int222.cfan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sit.int222.cfan.entities.Ingredients;
import sit.int222.cfan.entities.IngredientsType;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;
import sit.int222.cfan.repositories.IngredientsRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IngredientsController {
    @Autowired
    private IngredientsRepository ingredientsRepository;

    public List<Ingredients> findAll() {
        return ingredientsRepository.findAll();
    }

    public Ingredients findById(Long id) {
        Ingredients ingredients =ingredientsRepository.findById(id).orElse(null);
        if(ingredients == null){
            throw new BaseException(ExceptionResponse.ERROR_CODE.INGREDIENTS_DOES_NOT_EXIST, "Ingredients : id {" + id + "} does not exist !!");
        }
        return ingredients;
    }

    public Page<Ingredients> findPage(IngredientsType type, String searchData, Pageable pageable){
        if(type==null){
            return ingredientsRepository.findAllByIngredientsnameContaining(searchData,pageable);
        }
        return ingredientsRepository.findAllByIngredientsnameContainingAndIngredientstype(searchData,type,pageable);
    }

    public IngredientsType[] ingredientsType(){
        return IngredientsType.values();
    }
}
