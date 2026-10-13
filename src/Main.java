import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    static ArrayList<Expense> expenses = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n====================================");
            System.out.println("     PERSONAL EXPENSE ANALYZER");
            System.out.println("====================================");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Total Spending");
            System.out.println("4. View Category Analysis");
            System.out.println("5. Exit");

            System.out.print("\nEnter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    addExpense();
                    break;

                case 2:
                    viewExpenses();
                    break;

                case 3:
                    viewTotalSpending();
                    break;

                case 4:
                    viewCategoryAnalysis();
                    break;

                case 5:
                    System.out.println("Thank you for using Expense Analyzer!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 5);

        scanner.close();
    }

    static void addExpense() {

        System.out.print("Enter expense name: ");
        String name = scanner.nextLine();

        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }

        Expense expense = new Expense(name, category, amount);
        expenses.add(expense);

        System.out.println("Expense added successfully!");
    }

    static void viewExpenses() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses added yet.");
            return;
        }

        System.out.println("\n========== YOUR EXPENSES ==========");

        for (Expense expense : expenses) {
            System.out.printf(
                "%s | %s | Rs. %.2f%n",
                expense.getName(),
                expense.getCategory(),
                expense.getAmount()
            );
        }
    }

    static void viewTotalSpending() {

        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        System.out.printf("Total Spending: Rs. %.2f%n", total);
    }

    static void viewCategoryAnalysis() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses available for analysis.");
            return;
        }

        Map<String, Double> categoryTotals = new LinkedHashMap<>();

        for (Expense expense : expenses) {

            String category = expense.getCategory();
            double amount = expense.getAmount();

            double previousTotal =
                categoryTotals.getOrDefault(category, 0.0);

            categoryTotals.put(category, previousTotal + amount);
        }

        System.out.println("\n====== CATEGORY-WISE SPENDING ======");

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            System.out.printf(
                "%s: Rs. %.2f%n",
                entry.getKey(),
                entry.getValue()
            );
        }
    }
}