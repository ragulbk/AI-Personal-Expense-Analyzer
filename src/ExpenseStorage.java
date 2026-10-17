import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class ExpenseStorage {

    private static final String FILE_NAME = "expenses.txt";

    // Save expenses with their dates.
    public static void saveExpenses(ArrayList<Expense> expenses)
            throws IOException {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Expense expense : expenses) {

                writer.write(
                    expense.getName() + "|"
                    + expense.getCategory() + "|"
                    + expense.getAmount() + "|"
                    + expense.getDate()
                );

                writer.newLine();
            }
        }
    }

    // Load both old records and new records.
    public static ArrayList<Expense> loadExpenses()
            throws IOException {

        ArrayList<Expense> expenses = new ArrayList<>();

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return expenses;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|", -1);

                try {

                    // Read the common fields.
                    if (parts.length != 3 && parts.length != 4) {
                        System.out.println(
                            "Skipping invalid expense record: " + line
                        );
                        continue;
                    }

                    String name = parts[0].trim();
                    String category = parts[1].trim();
                    double amount = Double.parseDouble(parts[2]);

                    // Validate the expense information.
                    if (name.isBlank()
                            || category.isBlank()
                            || !Double.isFinite(amount)
                            || amount <= 0) {

                        System.out.println(
                            "Skipping invalid expense record: " + line
                        );
                        continue;
                    }

                    Expense expense;

                    if (parts.length == 4) {

                        // New record: use its saved date.
                        LocalDate date = LocalDate.parse(parts[3]);

                        expense = new Expense(
                            name, category, amount, date
                        );

                    } else {

                        // Old record: preserve it and assign today's date.
                        expense = new Expense(
                            name, category, amount
                        );
                    }

                    expenses.add(expense);

                } catch (NumberFormatException
                         | DateTimeParseException e) {

                    System.out.println(
                        "Skipping invalid expense record: " + line
                    );
                }
            }
        }

        return expenses;
    }
}