import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CategoryBudgetStorage {

    private static final String FILE_NAME = "category_budgets.txt";

    public static void saveBudgets(Map<String, Double> budgets)
            throws IOException {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Map.Entry<String, Double> entry : budgets.entrySet()) {

                writer.write(
                    entry.getKey() + "|" + entry.getValue()
                );

                writer.newLine();
            }
        }
    }

    public static Map<String, Double> loadBudgets()
            throws IOException {

        Map<String, Double> budgets = new HashMap<>();

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return budgets;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|", -1);

                if (parts.length != 2) {
                    continue;
                }

                String category = parts[0].trim();

                try {
                    double amount = Double.parseDouble(parts[1].trim());

                    if (!category.isBlank()
                            && Double.isFinite(amount)
                            && amount > 0) {

                        budgets.put(category, amount);
                    }

                } catch (NumberFormatException e) {
                    System.out.println(
                        "Skipping invalid category budget: " + line
                    );
                }
            }
        }

        return budgets;
    }
}