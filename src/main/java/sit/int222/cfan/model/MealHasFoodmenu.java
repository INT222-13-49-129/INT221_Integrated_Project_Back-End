package sit.int222.cfan.model;


public class MealHasFoodmenu {

  private long mealMealid;
  private long foodmenuFoodmenuid;
  private long totaldish;
  private long totalkcal;


  public long getMealMealid() {
    return mealMealid;
  }

  public void setMealMealid(long mealMealid) {
    this.mealMealid = mealMealid;
  }


  public long getFoodmenuFoodmenuid() {
    return foodmenuFoodmenuid;
  }

  public void setFoodmenuFoodmenuid(long foodmenuFoodmenuid) {
    this.foodmenuFoodmenuid = foodmenuFoodmenuid;
  }


  public long getTotaldish() {
    return totaldish;
  }

  public void setTotaldish(long totaldish) {
    this.totaldish = totaldish;
  }


  public long getTotalkcal() {
    return totalkcal;
  }

  public void setTotalkcal(long totalkcal) {
    this.totalkcal = totalkcal;
  }

}
