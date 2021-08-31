package sit.int222.cfan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.int222.cfan.entities.Meal;
import sit.int222.cfan.entities.User;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;
import sit.int222.cfan.repositories.MealRepository;

import java.util.List;

@Service
public class MealController {
    @Autowired
    private MealRepository mealRepository;

    public List<Meal> findAll() {
        return mealRepository.findAll();
    }

    public List<Meal> findMealsUser(User user) {
        return mealRepository.findAllByUser(user);
    }

    public Meal findByIdUser(User user, Long id) {
        Meal meal = mealRepository.findByUserAndMealid(user, id);
        if (meal == null) {
            throw new BaseException(ExceptionResponse.ERROR_CODE.MEAL_DOES_NOT_EXIST, "Meal : id {" + id + "} does not exist !!");
        }
        return meal;
    }

}
