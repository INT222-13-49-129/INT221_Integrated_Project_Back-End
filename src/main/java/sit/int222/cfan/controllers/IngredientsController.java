package sit.int222.cfan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sit.int222.cfan.entities.Foodmenu;
import sit.int222.cfan.entities.FoodmenuHasIngredients;
import sit.int222.cfan.entities.Ingredients;
import sit.int222.cfan.entities.IngredientsType;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;
import sit.int222.cfan.repositories.FoodmenuHasIngredientsRepository;
import sit.int222.cfan.repositories.IngredientsRepository;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

@Service
public class IngredientsController {
    @Autowired
    private IngredientsRepository ingredientsRepository;
    @Autowired
    private FoodmenuHasIngredientsRepository foodmenuHasIngredientsRepository;
    @Autowired
    private FoodmenuController foodmenuController;

    public long getCountIngredients() {
        return ingredientsRepository.count();
    }

    public List<Ingredients> findAll() {
        return ingredientsRepository.findAll();
    }

    public Ingredients findById(Long id) {
        Ingredients ingredients = ingredientsRepository.findById(id).orElse(null);
        if (ingredients == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.INGREDIENTS_DOES_NOT_EXIST, "Ingredients : id {" + id + "} does not exist !!");
        }
        return ingredients;
    }

    public Page<Ingredients> findPage(IngredientsType type, String searchData, Pageable pageable) {
        if (type == null) {
            return ingredientsRepository.findAllByIngredientsnameContaining(searchData, pageable);
        }
        return ingredientsRepository.findAllByIngredientsnameContainingAndIngredientstype(searchData, type, pageable);
    }

    public IngredientsType[] ingredientsType() {
        return IngredientsType.values();
    }

    public Ingredients createIngredients(Ingredients newingredients) {
        if (ingredientsRepository.existsByIngredientsname(newingredients.getIngredientsname())) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.INGREDIENTS_INGREDIENTSNAME_ALREADY_EXIST, "Ingredients : Ingredientsname {" + newingredients.getIngredientsname() + "} does already exist !!");
        }
        Ingredients ingredients = new Ingredients();
        ingredients.setIngredientsname(newingredients.getIngredientsname());
        ingredients.setKcalpunit(newingredients.getKcalpunit());
        ingredients.setUnit(newingredients.getUnit());
        ingredients.setDescriptionunit(newingredients.getDescriptionunit());
        ingredients.setIngredientstype(newingredients.getIngredientstype());
        return ingredientsRepository.save(ingredients);
    }

    public Ingredients updateIngredients(Ingredients updateingredients, Long id) {
        Ingredients ingredients = findById(id);
        if (ingredientsRepository.existsByIngredientsname(updateingredients.getIngredientsname()) && !updateingredients.getIngredientsname().equals(ingredients.getIngredientsname())) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.INGREDIENTS_INGREDIENTSNAME_ALREADY_EXIST, "Ingredients : Ingredientsname {" + updateingredients.getIngredientsname() + "} does already exist !!");
        }
        ingredients.setIngredientsname(updateingredients.getIngredientsname());
        ingredients.setKcalpunit(updateingredients.getKcalpunit());
        ingredients.setUnit(updateingredients.getUnit());
        ingredients.setDescriptionunit(updateingredients.getDescriptionunit());
        ingredients.setIngredientstype(updateingredients.getIngredientstype());
        ingredients = ingredientsRepository.save(ingredients);

        List<FoodmenuHasIngredients> foodmenuHasIngredientsList = foodmenuHasIngredientsRepository.findAllByIngredients(ingredients);
        ListIterator<FoodmenuHasIngredients> iterator = foodmenuHasIngredientsList.listIterator();
        while (iterator.hasNext()) {
            FoodmenuHasIngredients foodmenuHasIngredients = iterator.next();
            Foodmenu foodmenu = foodmenuController.findById(foodmenuHasIngredients.getFoodmenu().getFoodmenuid());
            foodmenuController.updateFoodmenu(foodmenu, null, foodmenu);
        }
        return ingredients;
    }

    public Map<String, Boolean> deleteIngredients(Long id) {
        Ingredients ingredients = findById(id);

        List<FoodmenuHasIngredients> foodmenuHasIngredientsList = foodmenuHasIngredientsRepository.findAllByIngredients(ingredients);
        ListIterator<FoodmenuHasIngredients> iterator = foodmenuHasIngredientsList.listIterator();

        ingredientsRepository.delete(ingredients);

        while (iterator.hasNext()) {
            FoodmenuHasIngredients foodmenuHasIngredients = iterator.next();
            Foodmenu foodmenu = foodmenuController.findById(foodmenuHasIngredients.getFoodmenu().getFoodmenuid());
            foodmenuController.updateFoodmenu(foodmenu, null, foodmenu);
        }

        HashMap<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return map;
    }
}
