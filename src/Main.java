import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.IOException;

public class Main {

    static ArrayList<Expense> expenses = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // Load previously saved expenses
        try {
            expenses = ExpenseStorage.loadExpenses();
            System.out.println(
                "Loaded " + expenses.size() + " saved expenses."
            );
        } catch (IOException e) {
            System.out.println(
                "Could not load saved expenses: " + e.getMessage()
            );
        }

        int choice;

        do {
            System.out.println("\n====================================");
            System.out.println("     PERSONAL EXPENSE ANALYZER");
            System.out.println("====================================");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Total Spending");
            System.out.println("4. View Category Analysis");
            System.out.println("5. Edit Expense");
            System.out.println("6. Delete Expense");
            System.out.println("7. Exit");

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
                    editExpense();
                    break;

                case 6:
                    deleteExpense();
                    break;

                case 7:
                    System.out.println(
                        "Thank you for using Expense Analyzer!"
                    );
                    break;

                default:
                    System.out.println(
                        "Invalid choice. Please try again."
                    );
            }

        } while (choice != 7);

        scanner.close();
    }

    // Add a new expense
    static void addExpense() {

        System.out.print("Enter expense name: ");
        String name = scanner.nextLine();

        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (name.isBlank() || category.isBlank()) {
            System.out.println(
                "Expense name and category cannot be empty."
            );
            return;
        }

        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println(
                "Amount must be a positive, valid number."
            );
            return;
        }

        Expense expense = new Expense(name, category, amount);
        expenses.add(expense);

        saveExpensesToFile();

        System.out.println("Expense added successfully!");
    }

    // Display all expenses
    static void viewExpenses() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses added yet.");
            return;
        }

        System.out.println("\n========== YOUR EXPENSES ==========");

        for (int i = 0; i < expenses.size(); i++) {

            Expense expense = expenses.get(i);

            System.out.printf(
                "%d. %s | %s | Rs. %.2f%n",
                i + 1,
                expense.getName(),
                expense.getCategory(),
                expense.getAmount()
            );
        }
    }

    // Calculate total spending
    static void viewTotalSpending() {

        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        System.out.printf(
            "Total Spending: Rs. %.2f%n",
            total
        );
    }

    // Calculate category-wise spending
    static void viewCategoryAnalysis() {

        if (expenses.isEmpty()) {
            System.out.println(
                "No expenses available for analysis."
            );
            return;
        }

        Map<String, Double> categoryTotals =
            new LinkedHashMap<>();

        for (Expense expense : expenses) {

            String category = expense.getCategory();
            double amount = expense.getAmount();

            double previousTotal =
                categoryTotals.getOrDefault(category, 0.0);

            categoryTotals.put(
                category,
                previousTotal + amount
            );
        }

        System.out.println(
            "\n====== CATEGORY-WISE SPENDING ======"
        );

        for (Map.Entry<String, Double> entry
                : categoryTotals.entrySet()) {

            System.out.printf(
                "%s: Rs. %.2f%n",
                entry.getKey(),
                entry.getValue()
            );
        }
    }

    // Edit an existing expense
    static void editExpense() {

        if (expenses.isEmpty()) {
            System.out.println(
                "No expenses available to edit."
            );
            return;
        }

        viewExpenses();

        System.out.print(
            "Enter the expense number to edit: "
        );

        int number = scanner.nextInt();
        scanner.nextLine();

        if (number < 1 || number > expenses.size()) {
            System.out.println("Invalid expense number.");
            return;
        }

        Expense oldExpense = expenses.get(number - 1);

        System.out.println(
            "Press Enter to keep an existing value."
        );

        System.out.print(
            "Enter new name (" + oldExpense.getName() + "): "
        );

        String name = scanner.nextLine();

        if (name.isBlank()) {
            name = oldExpense.getName();
        }

        System.out.print(
            "Enter new category (" +
            oldExpense.getCategory() + "): "
        );

        String category = scanner.nextLine();

        if (category.isBlank()) {
            category = oldExpense.getCategory();
        }

        System.out.print(
            "Enter new amount (" +
            oldExpense.getAmount() + "): "
        );

        String amountInput = scanner.nextLine();

        double amount;

        if (amountInput.isBlank()) {

            amount = oldExpense.getAmount();

        } else {

            try {
                amount = Double.parseDouble(amountInput);
            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid amount. Expense was not changed."
                );
                return;
            }

            if (!Double.isFinite(amount) || amount <= 0) {
                System.out.println(
                    "Amount must be a positive, valid number."
                );
                return;
            }
        }

        if (name.isBlank() || category.isBlank()) {
            System.out.println(
                "Name and category cannot be empty."
            );
            return;
        }

        Expense updatedExpense =
            new Expense(name, category, amount);

        expenses.set(number - 1, updatedExpense);

        saveExpensesToFile();

        System.out.println("Expense updated successfully!");
    }

    // Delete an expense
    static void deleteExpense() {

        if (expenses.isEmpty()) {
            System.out.println(
                "No expenses available to delete."
            );
            return;
        }

        viewExpenses();

        System.out.print(
            "Enter the expense number to delete: "
        );

        int number = scanner.nextInt();
        scanner.nextLine();

        if (number < 1 || number > expenses.size()) {
            System.out.println("Invalid expense number.");
            return;
        }

        Expense removedExpense =
            expenses.remove(number - 1);

        saveExpensesToFile();

        System.out.println(
            "Deleted expense: " + removedExpense.getName()
        );
    }

    // Save the current expense list to a file
    static void saveExpensesToFile() {

        try {
            ExpenseStorage.saveExpenses(expenses);

            System.out.println(
                "Expenses saved successfully!"
            );

        } catch (IOException e) {

            System.out.println(
                "Error saving expenses: " + e.getMessage()
            );
        }
    }
}