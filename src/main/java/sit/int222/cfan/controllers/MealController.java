package sit.int222.cfan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.int222.cfan.entities.Foodmenu;
import sit.int222.cfan.entities.Meal;
import sit.int222.cfan.entities.MealHasFoodmenu;
import sit.int222.cfan.entities.User;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;
import sit.int222.cfan.repositories.MealHasFoodmenuRepository;
import sit.int222.cfan.repositories.MealRepository;

import java.sql.Date;
import java.util.*;

@Service
public class MealController {
    @Autowired
    private MealRepository mealRepository;
    @Autowired
    private MealHasFoodmenuRepository mealHasFoodmenuRepository;
    @Autowired
    private FoodmenuController foodmenuController;

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

    public List<Meal> findByDateUser(User user, Date date) {
        return mealRepository.findByUserAndDatemeal(user, date);
    }

    public Meal createMeal(User user, Meal newmeal) {
        Meal meal = new Meal();
        meal.setMealtime(newmeal.getMealtime());
        meal.setDatemeal(newmeal.getDatemeal());
        meal.setUser(user);
        meal = mealRepository.save(meal);
        try {
            List<MealHasFoodmenu> listtotalkcal = calculatetotalkcalFoodmenu(newmeal.getMealHasFoodmenuList(), meal);
            meal.setMealHasFoodmenuList(listtotalkcal);
            meal.setTotalkcal(calculatetotalkcal(listtotalkcal));
        } catch (Exception e) {
            mealRepository.delete(meal);
            throw e;
        }
        return mealRepository.save(meal);
    }

    public Meal updateMeal(User user, Meal updatemeal, Long id) {
        Meal meal = findByIdUser(user, id);
        meal.setMealtime(updatemeal.getMealtime());
        meal.setDatemeal(updatemeal.getDatemeal());
        mealHasFoodmenuRepository.deleteAll(meal.getMealHasFoodmenuList());
        List<MealHasFoodmenu> listtotalkcal = calculatetotalkcalFoodmenu(updatemeal.getMealHasFoodmenuList(), meal);
        meal.setMealHasFoodmenuList(listtotalkcal);
        meal.setTotalkcal(calculatetotalkcal(listtotalkcal));
        return mealRepository.save(meal);
    }

    public Map<String, Boolean> deleteMeal(User user, Long id) {
        Meal meal = findByIdUser(user, id);
        mealHasFoodmenuRepository.deleteAll(meal.getMealHasFoodmenuList());
        mealRepository.delete(meal);
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return map;
    }

    public List<MealHasFoodmenu> calculatetotalkcalFoodmenu(List<MealHasFoodmenu> mealHasFoodmenuList, Meal meal) {
        List<MealHasFoodmenu> list = new ArrayList<>();
        ListIterator<MealHasFoodmenu> iterator = mealHasFoodmenuList.listIterator();
        while (iterator.hasNext()) {
            MealHasFoodmenu mealHasFoodmenu = iterator.next();
            mealHasFoodmenu.setMeal(meal);
            Foodmenu foodmenu = foodmenuController.findByIdPUBLISHorUser(meal.getUser(), mealHasFoodmenu.getKey().getFoodmenuFoodmenuid());
            mealHasFoodmenu.setFoodmenu(foodmenu);
            mealHasFoodmenu.getKey().setMealMealid(meal.getMealid());
            long totalkcal = mealHasFoodmenu.getFoodmenu().getTotalkcal() * mealHasFoodmenu.getTotaldish();
            mealHasFoodmenu.setTotalkcal(totalkcal);
            list.add(mealHasFoodmenuRepository.save(mealHasFoodmenu));
        }
        return list;
    }

    public long calculatetotalkcal(List<MealHasFoodmenu> mealHasFoodmenuList) {
        long totalkcal = 0;
        ListIterator<MealHasFoodmenu> iterator = mealHasFoodmenuList.listIterator();
        while (iterator.hasNext()) {
            MealHasFoodmenu mealHasFoodmenu = iterator.next();
            totalkcal += mealHasFoodmenu.getTotalkcal();
        }
        return totalkcal;
    }
}
