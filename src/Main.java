import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static ArrayList<Expense> expenses = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            expenses = ExpenseStorage.loadExpenses();
            System.out.println("Previous expenses loaded successfully.");
        } catch (IOException e) {
            System.out.println("Could not load expenses: " + e.getMessage());
        }

        int choice = 0;

        while (choice != 8) {
            System.out.println("\n===== PERSONAL EXPENSE ANALYZER =====");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Total Spending");
            System.out.println("4. View Category Analysis");
            System.out.println("5. Edit Expense");
            System.out.println("6. Delete Expense");
            System.out.println("7. Monthly Spending Report");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine();
                continue;
            }

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
                    System.out.println("Thank you for using Expense Analyzer!");
                    break;

                default:
                    System.out.println("Invalid choice. Please select 1-8.");
            }
        }

        scanner.close();
    }

    // Add a new expense
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

        Expense expense = new Expense(name, category, amount);
        expenses.add(expense);

        saveExpenses();

        System.out.println("Expense added successfully!");
    }

    // View all expenses
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
            System.out.println("Amount: Rs. " + expense.getAmount());
            System.out.println("Date: " + expense.getDate());
        }
    }

    // Calculate total spending
    private static void viewTotalSpending() {

        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        System.out.println("Total Spending: Rs. " + total);
    }

    // Analyze spending by category
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
            System.out.println(
                entry.getKey() + ": Rs. " + entry.getValue()
            );
        }
    }

    // Edit an existing expense
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

        Expense updatedExpense = new Expense(
            name,
            category,
            amount,
            oldExpense.getDate()
        );

        expenses.set(number - 1, updatedExpense);

        saveExpenses();

        System.out.println("Expense updated successfully!");
    }

    // Delete an expense
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

    // Generate a monthly spending report
    private static void monthlySpendingReport() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses available for the report.");
            return;
        }

        LocalDate today = LocalDate.now();

        int year = today.getYear();
        int month = today.getMonthValue();

        Map<String, Double> categoryTotals = new LinkedHashMap<>();
        double monthlyTotal = 0;

        for (Expense expense : expenses) {

            LocalDate expenseDate = expense.getDate();

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

        // Display the month as a readable name, such as October 2026.
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
            System.out.println(
                entry.getKey() + ": Rs. " + entry.getValue()
            );
        }

        System.out.println("\nTotal spending this month: Rs. " + monthlyTotal);
    }

    // Save expenses to the file
    private static void saveExpenses() {

        try {
            ExpenseStorage.saveExpenses(expenses);
        } catch (IOException e) {
            System.out.println("Could not save expenses: " + e.getMessage());
        }
    }
}