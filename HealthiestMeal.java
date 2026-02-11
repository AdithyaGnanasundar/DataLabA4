import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * DataLab Activity 4: What is the healthiest meal?
 * reads daily food nutrition data, computes health scores, and displays results graphically.
 */
public class HealthiestMeal {
    private static final String CSV_PATH = "daily_food_nutrition_dataset.csv";
    private ArrayList<FoodEntry> foodList;

    public static void main(String[] args) {
        HealthiestMeal app = new HealthiestMeal();
        app.loadData();
        app.processAndAnswer();
        app.displayGraphically();
    }

    /**
     * reads the CSV file and stores each row as a FoodEntry in an ArrayList.
     */
    private void loadData() {
        foodList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
            String header = br.readLine(); // Skip header
            if (header == null) {
                System.err.println("CSV file is empty.");
                return;
            }
            String line;
            while ((line = br.readLine()) != null) {
                FoodEntry entry = parseLine(line);
                if (entry != null) {
                    foodList.add(entry);
                }
            }
            System.out.println("Loaded " + foodList.size() + " food entries from " + CSV_PATH);
        } catch (FileNotFoundException e) {
            System.err.println("Data file not found: " + CSV_PATH + " (run from project folder)");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * parses one CSV line into a FoodEntry. handles food names that contain commas.
     */
    private FoodEntry parseLine(String line) {
        try {
            String[] tokens = line.split(",", -1);
            if (tokens.length < 12) return null;

            // if extra tokens (e.g. "Milk (2%, 1 cup)"), first field is food name with commas
            int n = tokens.length;
            String foodItem = n == 12 ? tokens[0].trim() : String.join(",", Arrays.copyOfRange(tokens, 0, n - 11)).trim();
            String category = tokens[n - 11].trim();
            double calories = parseDouble(tokens[n - 10]);
            double protein = parseDouble(tokens[n - 9]);
            double carbs = parseDouble(tokens[n - 8]);
            double fat = parseDouble(tokens[n - 7]);
            double fiber = parseDouble(tokens[n - 6]);
            double sugars = parseDouble(tokens[n - 5]);
            double sodium = parseDouble(tokens[n - 4]);
            double cholesterol = parseDouble(tokens[n - 3]);
            String mealType = tokens[n - 2].trim();
            double waterIntake = parseDouble(tokens[n - 1]);

            return new FoodEntry(foodItem, category, calories, protein, carbs, fat,
                    fiber, sugars, sodium, cholesterol, mealType, waterIntake);
        } catch (Exception e) {
            return null;
        }
    }

    private double parseDouble(String s) {
        if (s == null || s.trim().isEmpty()) return 0;
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * processes the data: sorts by health score and prints the answer to our question.
     */
    private void processAndAnswer() {
        if (foodList == null || foodList.isEmpty()) return;

        // sort by health score (descending)
        ArrayList<FoodEntry> sorted = new ArrayList<>(foodList);
        sorted.sort((a, b) -> Double.compare(b.getHealthScore(), a.getHealthScore()));

        System.out.println("\n=== What is the healthiest meal? ===\n");
        System.out.println("Top 10 healthiest food items (by our health score):\n");

        for (int i = 0; i < Math.min(10, sorted.size()); i++) {
            FoodEntry e = sorted.get(i);
            System.out.printf("%2d. %-45s  Score: %6.2f  (Protein: %.1fg, Fiber: %.1fg, Sodium: %.0fmg)%n",
                    i + 1, e.getFoodItem(), e.getHealthScore(), e.getProtein(), e.getFiber(), e.getSodium());
        }

        FoodEntry healthiest = sorted.get(0);
        System.out.println("\n*** Answer: The single healthiest food item in our dataset is: \""
                + healthiest.getFoodItem() + "\" (meal type: " + healthiest.getMealType() + ") ***\n");

        // also show healthiest by meal type
        System.out.println("Healthiest item per meal type:");
        Set<String> seen = new HashSet<>();
        for (FoodEntry e : sorted) {
            String type = e.getMealType();
            if (type != null && !type.isEmpty() && seen.add(type)) {
                System.out.println("  " + type + ": " + e.getFoodItem() + " (score: " + String.format("%.2f", e.getHealthScore()) + ")");
            }
        }
    }

    /**
     * graphically displays the data: a bar chart of the top 10 healthiest foods.
     */
    private void displayGraphically() {
        if (foodList == null || foodList.isEmpty()) return;

        ArrayList<FoodEntry> sorted = new ArrayList<>(foodList);
        sorted.sort((a, b) -> Double.compare(b.getHealthScore(), a.getHealthScore()));

        final int showCount = 10;
        final java.util.List<FoodEntry> top = new ArrayList<>(sorted.subList(0, Math.min(showCount, sorted.size())));

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("DataLab A4: What is the healthiest meal?");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 500);

            JPanel chartPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int w = getWidth();
                    int h = getHeight();
                    int left = 220;
                    int right = w - 40;
                    int chartTop = 40;
                    int bottom = h - 30;
                    int chartW = right - left;

                    double maxScore = top.stream().mapToDouble(FoodEntry::getHealthScore).max().orElse(1);

                    g2.setColor(Color.DARK_GRAY);
                    g2.drawString("Health score (higher = healthier)", left + chartW / 2 - 80, chartTop - 10);

                    for (int i = 0; i < top.size(); i++) {
                        FoodEntry e = top.get(i);
                        double score = e.getHealthScore();
                        int barLen = (int) ((score / maxScore) * (chartW - 80));
                        int y = chartTop + 25 + i * 38;
                        int barY = y - 8;

                        String label = e.getFoodItem();
                        if (label.length() > 28) label = label.substring(0, 25) + "...";
                        g2.setColor(Color.BLACK);
                        g2.drawString(label, 10, y + 4);
                        g2.setColor(new Color(60, 120, 80));
                        g2.fillRect(left, barY, barLen, 18);
                        g2.setColor(Color.BLACK);
                        g2.drawRect(left, barY, barLen, 18);
                        g2.drawString(String.format("%.1f", score), left + barLen + 5, y + 4);
                    }

                    g2.setColor(Color.GRAY);
                    g2.drawString("Top 10 healthiest foods (by protein, fiber; lower sodium, sugar, cholesterol)", 10, bottom + 20);
                }
            };
            chartPanel.setBackground(Color.WHITE);
            frame.add(chartPanel);
            frame.setVisible(true);
        });
    }
}
