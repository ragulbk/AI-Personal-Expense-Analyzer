import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        ArrayList<Expense> expenses = new ArrayList<>();

        System.out.println("================================");
        System.out.println("  PERSONAL EXPENSE ANALYZER");
        System.out.println("================================");

        System.out.print("How many expenses do you want to add? ");
        int count = scanner.nextInt();
        scanner.nextLine();

        // Enter expenses
        for (int i = 0; i < count; i++) {

            System.out.println("\nExpense " + (i + 1));

            System.out.print("Enter expense name: ");
            String name = scanner.nextLine();

            System.out.print("Enter category: ");
            String category = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            Expense expense = new Expense(name, category, amount);

            expenses.add(expense);

            System.out.println("Expense added successfully!");
        }

        // Store spending for each category
        Map<String, Double> categoryTotals = new LinkedHashMap<>();

        double total = 0;

        for (Expense expense : expenses) {

            double amount = expense.getAmount();
            String category = expense.getCategory();

            total += amount;

            double previousTotal =
                    categoryTotals.getOrDefault(category, 0.0);

            categoryTotals.put(category, previousTotal + amount);
        }

        // Display all expenses
        System.out.println("\n========== YOUR EXPENSES ==========");

        for (Expense expense : expenses) {

            System.out.printf(
                    "%s | %s | Rs. %.2f%n",
                    expense.getName(),
                    expense.getCategory(),
                    expense.getAmount()
            );
        }

        // Display analysis
        System.out.println("\n========== EXPENSE ANALYSIS ==========");

        System.out.printf("Total Spending: Rs. %.2f%n", total);

        System.out.println("\nCategory-wise Spending:");

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {

            System.out.printf(
                    "%s: Rs. %.2f%n",
                    entry.getKey(),
                    entry.getValue()
            );
        }

        scanner.close();
    }
}