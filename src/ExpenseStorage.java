import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ExpenseStorage {

    private static final String FILE_NAME = "expenses.txt";

    // Save all expenses to a file
    public static void saveExpenses(
            ArrayList<Expense> expenses) throws IOException {

        try (BufferedWriter writer =
                new BufferedWriter(
                    new FileWriter(FILE_NAME))) {

            for (Expense expense : expenses) {

                writer.write(
                    expense.getName() + "|" +
                    expense.getCategory() + "|" +
                    expense.getAmount()
                );

                writer.newLine();
            }
        }
    }

    // Load expenses from a file
    public static ArrayList<Expense> loadExpenses()
            throws IOException {

        ArrayList<Expense> expenses = new ArrayList<>();

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return expenses;
        }

        try (BufferedReader reader =
                new BufferedReader(
                    new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|", -1);

                if (parts.length != 3) {
                    System.out.println(
                        "Skipping invalid expense record."
                    );
                    continue;
                }

                try {
                    String name = parts[0];
                    String category = parts[1];

                    double amount =
                        Double.parseDouble(parts[2]);

                    if (name.isBlank()
                            || category.isBlank()
                            || !Double.isFinite(amount)
                            || amount <= 0) {

                        System.out.println(
                            "Skipping invalid expense record."
                        );
                        continue;
                    }

                    expenses.add(
                        new Expense(name, category, amount)
                    );

                } catch (NumberFormatException e) {

                    System.out.println(
                        "Skipping invalid expense record."
                    );
                }
            }
        }

        return expenses;
    }
}