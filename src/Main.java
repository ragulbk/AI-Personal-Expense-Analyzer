import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

public class Main {

    // Stores all expenses in memory while the program is running.
    private static ArrayList<Expense> expenses = new ArrayList<>();

    // Scanner reads user input from the keyboard.
    private static Scanner scanner = new Scanner(System.in);

    // Stores the monthly budget entered by the user.
    private static double monthlyBudget = 0.0;

    // Budget is saved separately so it can be loaded next time.
    private static final String BUDGET_FILE = "budget.txt";

    public static void main(String[] args) {

        // Load previously saved expenses.
        try {
            expenses = ExpenseStorage.loadExpenses();
            System.out.println("Previous expenses loaded successfully.");
        } catch (IOException e) {
            System.out.println("Could not load expenses: " + e.getMessage());
        }

        // Load the previously saved monthly budget.
        loadMonthlyBudget();

        int choice = 0;

        // Display the menu until the user chooses Exit.
        while (choice != 10) {

            System.out.println("\n===== AI PERSONAL EXPENSE ANALYZER =====");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Total Spending");
            System.out.println("4. View Category Analysis");
            System.out.println("5. Edit Expense");
            System.out.println("6. Delete Expense");
            System.out.println("7. Monthly Spending Report");
            System.out.println("8. Set Monthly Budget");
            System.out.println("9. View Monthly Budget");
            System.out.println("10. Exit");

            System.out.print("Enter your choice: ");

            // Validate the menu input.
            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine();
                continue;
            }

            choice = scanner.nextInt();
            scanner.nextLine();

            // Execute the selected menu option.
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
                    categoryAnalysis();
                    break;

                case 5:
                    editExpense();
                    break;

                case 6:
                    deleteExpense();
                    break;

                case 7:
                    monthlySpendingReport();
                    break;

                case 8:
                    setMonthlyBudget();
                    break;

                case 9:
                    viewMonthlyBudget();
                    break;

                case 10:
                    System.out.println("Thank you for using Expense Analyzer!");
                    break;

                default:
                    System.out.println("Invalid choice. Please select 1-10.");
            }
        }

        scanner.close();
    }

    // Add a new expense.
    private static void addExpense() {

        System.out.print("Enter expense name: ");
        String name = scanner.nextLine().trim();

        if (name.isBlank()) {
            System.out.println("Expense name cannot be empty.");
            return;
        }

        System.out.print("Enter category (Food, Travel, Shopping, etc.): ");
        String category = scanner.nextLine().trim();

        if (category.isBlank()) {
            System.out.println("Category cannot be empty.");
            return;
        }

        System.out.print("Enter amount: ");

        if (!scanner.hasNextDouble()) {
            System.out.println("Please enter a valid amount.");
            scanner.nextLine();
            return;
        }

        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }

        // Create the expense with today's date.
        Expense expense = new Expense(name, category, amount);

        expenses.add(expense);

        // Save expenses to the file.
        saveExpenses();

        System.out.println("Expense added successfully!");

        // Display a budget warning if a budget has been configured.
        showBudgetWarning();
    }

    // Display all recorded expenses.
    private static void viewExpenses() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        System.out.println("\n===== ALL EXPENSES =====");

        for (int i = 0; i < expenses.size(); i++) {

            Expense expense = expenses.get(i);

            System.out.println("\nExpense #" + (i + 1));
            System.out.println("Name: " + expense.getName());
            System.out.println("Category: " + expense.getCategory());
            System.out.printf("Amount: Rs. %.2f%n", expense.getAmount());
            System.out.println("Date: " + expense.getDate());
        }
    }

    // Calculate the total spending across all recorded expenses.
    private static void viewTotalSpending() {

        double total = 0.0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        System.out.printf("Total Spending: Rs. %.2f%n", total);
    }

    // Calculate how much was spent in each category.
    private static void categoryAnalysis() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses available for analysis.");
            return;
        }

        Map<String, Double> categoryTotals = new LinkedHashMap<>();

        for (Expense expense : expenses) {

            String category = expense.getCategory();

            categoryTotals.put(
                category,
                categoryTotals.getOrDefault(category, 0.0)
                    + expense.getAmount()
            );
        }

        System.out.println("\n===== CATEGORY ANALYSIS =====");

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {

            System.out.printf(
                "%s: Rs. %.2f%n",
                entry.getKey(),
                entry.getValue()
            );
        }
    }

    // Edit an existing expense without changing its original date.
    private static void editExpense() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses available to edit.");
            return;
        }

        viewExpenses();

        System.out.print("\nEnter the expense number to edit: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            scanner.nextLine();
            return;
        }

        int number = scanner.nextInt();
        scanner.nextLine();

        if (number < 1 || number > expenses.size()) {
            System.out.println("Invalid expense number.");
            return;
        }

        Expense oldExpense = expenses.get(number - 1);

        System.out.print("Enter new name: ");
        String name = scanner.nextLine().trim();

        if (name.isBlank()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        System.out.print("Enter new category: ");
        String category = scanner.nextLine().trim();

        if (category.isBlank()) {
            System.out.println("Category cannot be empty.");
            return;
        }

        System.out.print("Enter new amount: ");

        if (!scanner.hasNextDouble()) {
            System.out.println("Please enter a valid amount.");
            scanner.nextLine();
            return;
        }

        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }

        // Preserve the original expense date.
        Expense updatedExpense = new Expense(
            name,
            category,
            amount,
            oldExpense.getDate()
        );

        expenses.set(number - 1, updatedExpense);

        saveExpenses();

        System.out.println("Expense updated successfully!");

        showBudgetWarning();
    }

    // Delete an expense.
    private static void deleteExpense() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses available to delete.");
            return;
        }

        viewExpenses();

        System.out.print("\nEnter the expense number to delete: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            scanner.nextLine();
            return;
        }

        int number = scanner.nextInt();
        scanner.nextLine();

        if (number < 1 || number > expenses.size()) {
            System.out.println("Invalid expense number.");
            return;
        }

        Expense removedExpense = expenses.remove(number - 1);

        saveExpenses();

        System.out.println(
            "Deleted expense: " + removedExpense.getName()
        );
    }

    // Display a report for the current month.
    private static void monthlySpendingReport() {

        LocalDate today = LocalDate.now();

        int year = today.getYear();
        int month = today.getMonthValue();

        Map<String, Double> categoryTotals = new LinkedHashMap<>();

        double monthlyTotal = 0.0;

        for (Expense expense : expenses) {

            LocalDate expenseDate = expense.getDate();

            // Include only expenses from the current month and year.
            if (expenseDate.getYear() == year
                    && expenseDate.getMonthValue() == month) {

                String category = expense.getCategory();

                categoryTotals.put(
                    category,
                    categoryTotals.getOrDefault(category, 0.0)
                        + expense.getAmount()
                );

                monthlyTotal += expense.getAmount();
            }
        }

        // Convert the month number into a readable month name.
        String monthName = Month.of(month).name();

        monthName = monthName.substring(0, 1)
                + monthName.substring(1).toLowerCase();

        System.out.println("\n===== MONTHLY SPENDING REPORT =====");
        System.out.println("Month: " + monthName + " " + year);

        if (categoryTotals.isEmpty()) {
            System.out.println("No expenses recorded for this month.");
            return;
        }

        System.out.println("\nSpending by category:");

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {

            System.out.printf(
                "%s: Rs. %.2f%n",
                entry.getKey(),
                entry.getValue()
            );
        }

        System.out.printf(
            "%nTotal spending this month: Rs. %.2f%n",
            monthlyTotal
        );
    }

    // Allow the user to set or update the monthly budget.
    private static void setMonthlyBudget() {

        System.out.println("\n===== SET MONTHLY BUDGET =====");

        System.out.print("Enter your monthly budget in Rs.: ");

        if (!scanner.hasNextDouble()) {
            System.out.println("Please enter a valid amount.");
            scanner.nextLine();
            return;
        }

        double budget = scanner.nextDouble();
        scanner.nextLine();

        if (!Double.isFinite(budget) || budget <= 0) {
            System.out.println("Budget must be greater than zero.");
            return;
        }

        monthlyBudget = budget;

        // Save the budget so it remains available after restarting.
        saveMonthlyBudget();

        System.out.printf(
            "Monthly budget set to Rs. %.2f%n",
            monthlyBudget
        );

        // Show the current month's budget status.
        viewMonthlyBudget();
    }

    // Display the current month's budget and spending.
    private static void viewMonthlyBudget() {

        System.out.println("\n===== MONTHLY BUDGET TRACKER =====");

        if (monthlyBudget <= 0) {
            System.out.println("No monthly budget has been set yet.");
            System.out.println("Choose option 8 to set your budget.");
            return;
        }

        double monthlyTotal = getCurrentMonthSpending();

        double remaining = monthlyBudget - monthlyTotal;

        double percentageUsed = (monthlyTotal / monthlyBudget) * 100.0;

        System.out.printf(
            "Monthly Budget: Rs. %.2f%n",
            monthlyBudget
        );

        System.out.printf(
            "Total Spent:    Rs. %.2f%n",
            monthlyTotal
        );

        if (remaining >= 0) {

            System.out.printf(
                "Remaining:      Rs. %.2f%n",
                remaining
            );

        } else {

            System.out.printf(
                "Overspending:   Rs. %.2f%n",
                Math.abs(remaining)
            );
        }

        System.out.printf(
            "Budget Used:    %.1f%%%n",
            percentageUsed
        );

        // Display a warning based on the amount spent.
        if (monthlyTotal > monthlyBudget) {

            System.out.println(
                "WARNING: You have exceeded your monthly budget!"
            );

        } else if (percentageUsed >= 80.0) {

            System.out.println(
                "WARNING: You have used at least 80% of your budget."
            );

        } else {

            System.out.println(
                "You are within your monthly budget."
            );
        }
    }

    // Calculate spending for the current month only.
    private static double getCurrentMonthSpending() {

        LocalDate today = LocalDate.now();

        double total = 0.0;

        for (Expense expense : expenses) {

            LocalDate expenseDate = expense.getDate();

            if (expenseDate.getYear() == today.getYear()
                    && expenseDate.getMonthValue() == today.getMonthValue()) {

                total += expense.getAmount();
            }
        }

        return total;
    }

    // Display a budget warning after an expense is added or edited.
    private static void showBudgetWarning() {

        if (monthlyBudget <= 0) {
            return;
        }

        double total = getCurrentMonthSpending();

        double percentageUsed = (total / monthlyBudget) * 100.0;

        if (total > monthlyBudget) {

            System.out.println(
                "\nWARNING: You have exceeded your monthly budget!"
            );

            System.out.printf(
                "Overspending: Rs. %.2f%n",
                total - monthlyBudget
            );

        } else if (percentageUsed >= 80.0) {

            System.out.println(
                "\nWARNING: You have used at least 80% of your monthly budget."
            );
        }
    }

    // Save expenses to the existing expense file.
    private static void saveExpenses() {

        try {
            ExpenseStorage.saveExpenses(expenses);

        } catch (IOException e) {

            System.out.println(
                "Could not save expenses: " + e.getMessage()
            );
        }
    }

    // Save the monthly budget to a separate file.
    private static void saveMonthlyBudget() {

        try (PrintWriter writer = new PrintWriter(
                new FileWriter(BUDGET_FILE))) {

            writer.println(monthlyBudget);

        } catch (IOException e) {

            System.out.println(
                "Could not save the monthly budget: " + e.getMessage()
            );
        }
    }

    // Load the previously saved monthly budget when the program starts.
    private static void loadMonthlyBudget() {

        File file = new File(BUDGET_FILE);

        if (!file.exists()) {
            System.out.println("No saved monthly budget found yet.");
            return;
        }

        try (BufferedReader reader =
                new BufferedReader(new FileReader(file))) {

            String line = reader.readLine();

            if (line != null) {

                double savedBudget = Double.parseDouble(line.trim());

                if (Double.isFinite(savedBudget) && savedBudget > 0) {

                    monthlyBudget = savedBudget;

                    System.out.printf(
                        "Saved monthly budget loaded: Rs. %.2f%n",
                        monthlyBudget
                    );

                } else {

                    System.out.println(
                        "Saved budget is invalid. Please set a new budget."
                    );
                }
            }

        } catch (IOException | NumberFormatException e) {

            System.out.println(
                "Could not load the monthly budget: " + e.getMessage()
            );
        }
    }
}