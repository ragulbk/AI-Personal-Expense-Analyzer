// Import ArrayList to store multiple expenses.
import java.util.ArrayList;

// Import LinkedHashMap and Map for category-wise spending analysis.
import java.util.LinkedHashMap;
import java.util.Map;

// Import Scanner to read input from the keyboard.
import java.util.Scanner;

// Import IOException to handle file-related errors.
import java.io.IOException;

// Main class of the Personal Expense Analyzer.
public class Main {

    // Stores all expenses added by the user.
    static ArrayList<Expense> expenses = new ArrayList<>();

    // Reads input entered by the user.
    static Scanner scanner = new Scanner(System.in);

    // Program execution starts here.
    public static void main(String[] args) {

        // Load previously saved expenses when the program starts.
        try {
            expenses = ExpenseStorage.loadExpenses();

            System.out.println(
                "Loaded " + expenses.size() + " saved expenses."
            );

        } catch (IOException e) {

            // Display a message if saved expenses cannot be loaded.
            System.out.println(
                "Could not load saved expenses: " + e.getMessage()
            );
        }

        // Stores the user's menu selection.
        int choice = 0;

        // Repeat the menu until the user chooses Exit.
        do {

            // Display the application menu.
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

            // Check whether the user entered a whole number.
            if (scanner.hasNextInt()) {

                // Read the menu selection.
                choice = scanner.nextInt();

                // Remove the leftover newline from the input.
                scanner.nextLine();

            } else {

                // Handle letters or other invalid menu input.
                System.out.println(
                    "Invalid input! Please enter a number."
                );

                // Clear the invalid input.
                scanner.nextLine();

                // Restart the menu.
                continue;
            }

            // Perform the operation selected by the user.
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

        // Continue until the user chooses option 7.
        } while (choice != 7);

        // Close the keyboard input reader.
        scanner.close();
    }

    // Adds a new expense to the application.
    static void addExpense() {

        // Ask for the expense name.
        System.out.print("Enter expense name: ");

        // Read the name and remove extra spaces at the beginning and end.
        String name = scanner.nextLine().trim();

        // Reject an empty expense name.
        if (name.isEmpty()) {
            System.out.println("Expense name cannot be empty.");
            return;
        }

        // Ask for the expense category.
        System.out.print("Enter category: ");

        // Read the category.
        String category = scanner.nextLine().trim();

        // Reject an empty category.
        if (category.isEmpty()) {
            System.out.println("Category cannot be empty.");
            return;
        }

        // Ask for the expense amount.
        System.out.print("Enter amount: ");

        // Read the amount as text before converting it to a number.
        String amountInput = scanner.nextLine().trim();

        // Declare a variable to store the numeric amount.
        double amount;

        // Convert the entered text into a decimal number.
        try {
            amount = Double.parseDouble(amountInput);

        } catch (NumberFormatException e) {

            // Handle input such as "hello" instead of a number.
            System.out.println(
                "Invalid amount! Please enter a number."
            );

            return;
        }

        // Reject zero, negative values, infinity, and NaN.
        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println(
                "Amount must be a finite number greater than zero."
            );

            return;
        }

        // Create an Expense object using the entered information.
        Expense expense = new Expense(name, category, amount);

        // Add the new expense to the ArrayList.
        expenses.add(expense);

        // Confirm that the expense was added.
        System.out.println("Expense added successfully!");

        // Save the updated expense list to the file.
        saveExpensesToFile();
    }

    // Displays every saved expense.
    static void viewExpenses() {

        // Check whether the list is empty.
        if (expenses.isEmpty()) {
            System.out.println("No expenses added yet.");
            return;
        }

        // Display the heading.
        System.out.println("\n========== YOUR EXPENSES ==========");

        // Loop through the expense list.
        for (int i = 0; i < expenses.size(); i++) {

            // Get the expense at the current position.
            Expense expense = expenses.get(i);

            // Display its number, name, category, and amount.
            System.out.printf(
                "%d. %s | %s | Rs. %.2f%n",
                i + 1,
                expense.getName(),
                expense.getCategory(),
                expense.getAmount()
            );
        }
    }

    // Calculates and displays the total spending.
    static void viewTotalSpending() {

        // Start the total at zero.
        double total = 0;

        // Add the amount of every expense.
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        // Display the total with two decimal places.
        System.out.printf(
            "Total Spending: Rs. %.2f%n",
            total
        );
    }

    // Calculates spending separately for each category.
    static void viewCategoryAnalysis() {

        // Check whether there are any expenses to analyze.
        if (expenses.isEmpty()) {
            System.out.println(
                "No expenses available for analysis."
            );

            return;
        }

        // Store each category and its total spending.
        // LinkedHashMap preserves the order in which categories appear.
        Map<String, Double> categoryTotals = new LinkedHashMap<>();

        // Process each expense.
        for (Expense expense : expenses) {

            // Get the current expense's category.
            String category = expense.getCategory();

            // Get the current expense's amount.
            double amount = expense.getAmount();

            // Find the category's existing total.
            // If the category is new, its starting total is zero.
            double previousTotal =
                categoryTotals.getOrDefault(category, 0.0);

            // Add the current amount to that category's total.
            categoryTotals.put(
                category,
                previousTotal + amount
            );
        }

        // Display the category analysis heading.
        System.out.println("\n====== CATEGORY-WISE SPENDING ======");

        // Display the total for every category.
        for (Map.Entry<String, Double> entry
                : categoryTotals.entrySet()) {

            System.out.printf(
                "%s: Rs. %.2f%n",
                entry.getKey(),
                entry.getValue()
            );
        }
    }

    // Allows the user to update an existing expense.
    static void editExpense() {

        // Check whether any expenses exist.
        if (expenses.isEmpty()) {
            System.out.println(
                "No expenses available to edit."
            );

            return;
        }

        // Display expenses so the user can choose one.
        viewExpenses();

        // Ask which expense should be edited.
        System.out.print("Enter the expense number to edit: ");

        // Ensure the selection is a whole number.
        if (!scanner.hasNextInt()) {
            System.out.println(
                "Invalid input! Please enter a number."
            );

            scanner.nextLine();
            return;
        }

        // Read the selected expense number.
        int number = scanner.nextInt();

        // Clear the leftover newline.
        scanner.nextLine();

        // Ensure the number matches an existing expense.
        if (number < 1 || number > expenses.size()) {
            System.out.println("Invalid expense number.");
            return;
        }

        // ArrayList positions begin at zero, so subtract one.
        Expense oldExpense = expenses.get(number - 1);

        // Tell the user that pressing Enter keeps the old value.
        System.out.println(
            "Press Enter to keep the existing value."
        );

        // Ask for a replacement name.
        System.out.print(
            "Enter new name (" + oldExpense.getName() + "): "
        );

        String name = scanner.nextLine().trim();

        // Keep the previous name if the input is empty.
        if (name.isEmpty()) {
            name = oldExpense.getName();
        }

        // Ask for a replacement category.
        System.out.print(
            "Enter new category (" + oldExpense.getCategory() + "): "
        );

        String category = scanner.nextLine().trim();

        // Keep the previous category if the input is empty.
        if (category.isEmpty()) {
            category = oldExpense.getCategory();
        }

        // Ask for a replacement amount.
        System.out.print(
            "Enter new amount (" + oldExpense.getAmount() + "): "
        );

        String amountInput = scanner.nextLine().trim();

        // Declare the updated amount.
        double amount;

        // If no amount is entered, retain the previous amount.
        if (amountInput.isEmpty()) {

            amount = oldExpense.getAmount();

        } else {

            // Convert the new amount into a number.
            try {
                amount = Double.parseDouble(amountInput);

            } catch (NumberFormatException e) {

                System.out.println(
                    "Invalid amount. Expense was not changed."
                );

                return;
            }

            // Reject invalid amounts.
            if (!Double.isFinite(amount) || amount <= 0) {
                System.out.println(
                    "Amount must be greater than zero. "
                    + "Expense was not changed."
                );

                return;
            }
        }

        // Create a replacement Expense object.
        Expense updatedExpense =
            new Expense(name, category, amount);

        // Replace the selected expense in the list.
        expenses.set(number - 1, updatedExpense);

        // Confirm the update.
        System.out.println("Expense updated successfully!");

        // Save the updated list to the file.
        saveExpensesToFile();
    }

    // Deletes an expense selected by the user.
    static void deleteExpense() {

        // Check whether any expenses exist.
        if (expenses.isEmpty()) {
            System.out.println(
                "No expenses available to delete."
            );

            return;
        }

        // Display the current expenses.
        viewExpenses();

        // Ask which expense should be deleted.
        System.out.print(
            "Enter the expense number to delete: "
        );

        // Check that the selection is a whole number.
        if (!scanner.hasNextInt()) {
            System.out.println(
                "Invalid input! Please enter a number."
            );

            scanner.nextLine();
            return;
        }

        // Read the selected expense number.
        int number = scanner.nextInt();

        // Clear the leftover newline.
        scanner.nextLine();

        // Validate the selected number.
        if (number < 1 || number > expenses.size()) {
            System.out.println("Invalid expense number.");
            return;
        }

        // Remove the selected expense from the list.
        Expense removedExpense = expenses.remove(number - 1);

        // Display the name of the deleted expense.
        System.out.println(
            "Deleted expense: " + removedExpense.getName()
        );

        // Save the remaining expenses to the file.
        saveExpensesToFile();
    }

    // Saves the current expense list to the storage file.
    static void saveExpensesToFile() {

        // Attempt to write the expenses to the file.
        try {

            ExpenseStorage.saveExpenses(expenses);

            // Confirm successful saving.
            System.out.println(
                "Expenses saved successfully!"
            );

        } catch (IOException e) {

            // Display an error if the file cannot be saved.
            System.out.println(
                "Error saving expenses: " + e.getMessage()
            );
        }
    }
}