# DataLab Activity 4: What is the healthiest meal?

**Adithya Gnanasundar, Sahiel Bose, Ashton Lu, Vedant Chauhan** — CSA Period 4

---

This program answers the question *What is the healthiest meal?* using the daily food nutrition dataset. It reads the CSV into an `ArrayList` of `FoodEntry` objects, computes a health score for each item (favoring protein and fiber, penalizing sodium, sugar, and cholesterol), and prints the top 10 healthiest foods and the healthiest item per meal type. A bar chart window shows the top 10 results graphically.

**Run from the project folder:** `javac FoodEntry.java HealthiestMeal.java` then `java HealthiestMeal`. The CSV file `daily_food_nutrition_dataset.csv` must be in the same directory.
