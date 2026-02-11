# DataLab Activity 4: What is the healthiest meal?

## Question of interest
**What is the healthiest meal?**

We use a daily food nutrition dataset to score each food item and identify the healthiest options.

---

## Brainstorming

- **Possible questions:** “What is the healthiest meal?”, “Which meal type has the best options?”, “What are the top 10 healthiest foods?”
- **Chosen question:** “What is the healthiest meal?” — we answer it by defining a health score and ranking all foods.
- **Health score:** We favor **protein** and **fiber**, and penalize **sodium**, **sugars**, **cholesterol**, and very **high calories**. The formula is applied in the `FoodEntry` class.
- **Output:** Top 10 healthiest foods in the console, healthiest per meal type, and a **bar chart** of the top 10.

---

## Milestone chart

| Milestone | Description | Status |
|-----------|-------------|--------|
| 1 | Formulate question and get dataset | Done |
| 2 | Create `FoodEntry` class for one row | Done |
| 3 | Read CSV into `ArrayList<FoodEntry>` in `main` | Done |
| 4 | Implement health score and sort to answer the question | Done |
| 5 | Add graphical display (bar chart of top 10) | Done |
| 6 | Test and document for presentation | Done |

---

## Data source and format

- **Source:** Daily food nutrition dataset (`daily_food_nutrition_dataset.csv`), provided in the project folder.
- **Format:** CSV with header row. Columns:
  - `Food_Item`, `Category`, `Calories (kcal)`, `Protein (g)`, `Carbohydrates (g)`, `Fat (g)`, `Fiber (g)`, `Sugars (g)`, `Sodium (mg)`, `Cholesterol (mg)`, `Meal_Type`, `Water_Intake (ml)`
- **Access:** The program reads the file from the **current working directory** using `BufferedReader` and `FileReader`. The path used is `daily_food_nutrition_dataset.csv`, so you must **run the program from the project folder** (where the CSV lives).

---

## How to run

1. Put `daily_food_nutrition_dataset.csv` in the same folder as the Java files.
2. From that folder, compile and run:
   ```bash
   javac FoodEntry.java HealthiestMeal.java
   java HealthiestMeal
   ```
3. Console output shows the top 10 healthiest foods and the healthiest per meal type; a window opens with a bar chart of the top 10.

---

## Code samples (for presentation)

### 1. Class modeling one entry (`FoodEntry.java`)

```java
public class FoodEntry {
    private String foodItem;
    private String category;
    private double calories, protein, carbohydrates, fat, fiber, sugars, sodium, cholesterol;
    private String mealType;
    private double waterIntake;

    public FoodEntry(String foodItem, String category, double calories, double protein, ...) {
        this.foodItem = foodItem;
        this.calories = calories;
        // ... assign all fields
    }

    public double getHealthScore() {
        double score = 0;
        score += protein * 1.5;       // favor protein
        score += fiber * 2.0;         // favor fiber
        score -= sodium / 150.0;      // penalize sodium
        score -= sugars / 3.0;        // penalize sugar
        score -= cholesterol / 40.0;  // penalize cholesterol
        if (calories > 400) score -= (calories - 400) / 80.0;
        return Math.max(0, score);
    }
    // getters for all fields...
}
```

### 2. Reading data into an `ArrayList` (`HealthiestMeal.java`)

```java
private void loadData() {
    foodList = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
        br.readLine(); // skip header
        String line;
        while ((line = br.readLine()) != null) {
            FoodEntry entry = parseLine(line);
            if (entry != null) foodList.add(entry);
        }
    }
    // ...
}
```

### 3. Processing data to answer the question

```java
ArrayList<FoodEntry> sorted = new ArrayList<>(foodList);
sorted.sort((a, b) -> Double.compare(b.getHealthScore(), a.getHealthScore()));
FoodEntry healthiest = sorted.get(0);
// Print top 10 and healthiest per meal type...
```

### 4. Graphical display

The program uses **Swing**: a `JFrame` with a `JPanel` that overrides `paintComponent(Graphics g)` to draw a **bar chart** of the top 10 foods by health score (labels on the left, horizontal bars on the right).

---

## Requirements checklist

- [x] Formulate a question: *What is the healthiest meal?*
- [x] Identify a dataset: *daily_food_nutrition_dataset.csv*
- [x] Create a class for one entry: *`FoodEntry`*
- [x] Program with `main` that reads the dataset: *`HealthiestMeal.main` → `loadData()`*
- [x] Use an array or `ArrayList` and the class to store data: *`ArrayList<FoodEntry> foodList`*
- [x] Process data and determine an answer: *health score, sort, top 10 and healthiest per meal type*
- [x] Graphically display data: *Swing bar chart of top 10 healthiest foods*
