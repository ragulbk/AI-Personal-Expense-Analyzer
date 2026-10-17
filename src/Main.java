import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.IOException;

public class Main {

    // Stores all expenses.
    static ArrayList<Expense> expenses = new ArrayList<>();

    // Reads input entered by the user.
    static Scanner scanner = new Scanner(System.in);

    // Main method: program execution starts here.
    public static void main(String[] args) {

        // Load saved expenses when the program starts.
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

        int choice = 0;

        // Display the menu repeatedly until the user exits.
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

            // Validate menu input.
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println(
                    "Invalid input! Please enter a number."
                );
                scanner.nextLine();
                continue;
            }

            // Execute the selected operation.
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
                        "Invalid choice. Please select 1 to 7."
                    );
            }

        } while (choice != 7);

        scanner.close();
    }

    // Adds a new expense.
    static void addExpense() {

        System.out.print("Enter expense name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Expense name cannot be empty.");
            return;
        }

        System.out.print("Enter category: ");
        String category = scanner.nextLine().trim();

        if (category.isEmpty()) {
            System.out.println("Category cannot be empty.");
            return;
        }

        System.out.print("Enter amount: ");
        String amountInput = scanner.nextLine().trim();

        double amount;

        // Convert the amount to a number.
        try {
            amount = Double.parseDouble(amountInput);
        } catch (NumberFormatException e) {
            System.out.println(
                "Invalid amount! Please enter a number."
            );
            return;
        }

        // Amount must be positive and finite.
        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println(
                "Amount must be a finite number greater than zero."
            );
            return;
        }

        // Create an expense with today's date.
        Expense expense = new Expense(name, category, amount);

        // Add the expense to the list.
        expenses.add(expense);

        System.out.println("Expense added successfully!");

        // Save changes to the file.
        saveExpensesToFile();
    }

    // Displays all expenses, including their dates.
    static void viewExpenses() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses added yet.");
            return;
        }

        System.out.println("\n================ YOUR EXPENSES ================");

        // Loop through all expenses.
        for (int i = 0; i < expenses.size(); i++) {

            Expense expense = expenses.get(i);

            // Display the expense details and date.
            System.out.printf(
                "%d. %s | %s | Rs. %.2f | Date: %s%n",
                i + 1,
                expense.getName(),
                expense.getCategory(),
                expense.getAmount(),
                expense.getDate()
            );
        }
    }

    // Calculates the total amount spent.
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

    // Calculates spending for each category.
    static void viewCategoryAnalysis() {

        if (expenses.isEmpty()) {
            System.out.println(
                "No expenses available for analysis."
            );
            return;
        }

        // Maps category names to their spending totals.
        Map<String, Double> categoryTotals =
            new LinkedHashMap<>();

        for (Expense expense : expenses) {

            String category = expense.getCategory();
            double amount = expense.getAmount();

            // Get the existing total, or zero for a new category.
            double previousTotal =
                categoryTotals.getOrDefault(category, 0.0);

            // Update the category's total.
            categoryTotals.put(
                category,
                previousTotal + amount
            );
        }

        System.out.println("\n====== CATEGORY-WISE SPENDING ======");

        // Display each category's total.
        for (Map.Entry<String, Double> entry
                : categoryTotals.entrySet()) {

            System.out.printf(
                "%s: Rs. %.2f%n",
                entry.getKey(),
                entry.getValue()
            );
        }
    }

    // Edits an existing expense.
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

        if (!scanner.hasNextInt()) {
            System.out.println(
                "Invalid input! Please enter a number."
            );
            scanner.nextLine();
            return;
        }

        int number = scanner.nextInt();
        scanner.nextLine();

        // Check that the selected expense exists.
        if (number < 1 || number > expenses.size()) {
            System.out.println("Invalid expense number.");
            return;
        }

        Expense oldExpense = expenses.get(number - 1);

        System.out.println(
            "Press Enter to keep the existing value."
        );

        // Request a new name.
        System.out.print(
            "Enter new name (" + oldExpense.getName() + "): "
        );

        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            name = oldExpense.getName();
        }

        // Request a new category.
        System.out.print(
            "Enter new category (" + oldExpense.getCategory() + "): "
        );

        String category = scanner.nextLine().trim();

        if (category.isEmpty()) {
            category = oldExpense.getCategory();
        }

        // Request a new amount.
        System.out.print(
            "Enter new amount (" + oldExpense.getAmount() + "): "
        );

        String amountInput = scanner.nextLine().trim();

        double amount;

        if (amountInput.isEmpty()) {

            // Keep the existing amount.
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
                    "Amount must be greater than zero. "
                    + "Expense was not changed."
                );
                return;
            }
        }

        // Preserve the existing date when editing an expense.
        Expense updatedExpense = new Expense(
            name,
            category,
            amount,
            oldExpense.getDate()
        );

        // Replace the old expense with the updated one.
        expenses.set(number - 1, updatedExpense);

        System.out.println("Expense updated successfully!");

        saveExpensesToFile();
    }

    // Deletes an expense.
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

        if (!scanner.hasNextInt()) {
            System.out.println(
                "Invalid input! Please enter a number."
            );
            scanner.nextLine();
            return;
        }

        int number = scanner.nextInt();
        scanner.nextLine();

        if (number < 1 || number > expenses.size()) {
            System.out.println("Invalid expense number.");
            return;
        }

        // Remove the selected expense.
        Expense removedExpense = expenses.remove(number - 1);

        System.out.println(
            "Deleted expense: " + removedExpense.getName()
        );

        saveExpensesToFile();
    }

    // Saves expenses using ExpenseStorage.
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