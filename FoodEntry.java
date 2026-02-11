/**
 * FoodEntry - models one entry (row) from the daily food nutrition dataset.
 * used to store and process nutritional data for the "What is the healthiest meal?" analysis.
 */
public class FoodEntry {
    private String foodItem;
    private String category;
    private double calories;
    private double protein;
    private double carbohydrates;
    private double fat;
    private double fiber;
    private double sugars;
    private double sodium;
    private double cholesterol;
    private String mealType;
    private double waterIntake;

    public FoodEntry(String foodItem, String category, double calories, double protein,
                     double carbohydrates, double fat, double fiber, double sugars,
                     double sodium, double cholesterol, String mealType, double waterIntake) {
        this.foodItem = foodItem;
        this.category = category;
        this.calories = calories;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fat = fat;
        this.fiber = fiber;
        this.sugars = sugars;
        this.sodium = sodium;
        this.cholesterol = cholesterol;
        this.mealType = mealType;
        this.waterIntake = waterIntake;
    }

    /**
     * computes a "health score" for this food item.
     * higher score = healthier. favors protein and fiber; penalizes sodium, sugars, cholesterol, excess calories.
     */
    public double getHealthScore() {
        double score = 0;
        score += protein * 1.5;           // protein is good
        score += fiber * 2.0;             // fiber is good
        score -= sodium / 150.0;          // High sodium is bad
        score -= sugars / 3.0;             // high sugar is bad
        score -= cholesterol / 40.0;      // high cholesterol is bad
        if (calories > 400) {
            score -= (calories - 400) / 80.0;  // very high calories penalized
        }
        return Math.max(0, score);
    }

    // getters
    public String getFoodItem()      { return foodItem; }
    public String getCategory()      { return category; }
    public double getCalories()      { return calories; }
    public double getProtein()       { return protein; }
    public double getCarbohydrates() { return carbohydrates; }
    public double getFat()           { return fat; }
    public double getFiber()         { return fiber; }
    public double getSugars()        { return sugars; }
    public double getSodium()        { return sodium; }
    public double getCholesterol()    { return cholesterol; }
    public String getMealType()       { return mealType; }
    public double getWaterIntake()    { return waterIntake; }
}
