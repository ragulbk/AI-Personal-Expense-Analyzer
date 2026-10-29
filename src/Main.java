import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static ArrayList<Expense> expenses = new ArrayList<>();
private static final Scanner scanner = new Scanner(System.in);
private static double monthlyBudget = 0.0;
private static final String BUDGET_FILE = "budget.txt";
private static final String CSV_FILE = "expenses.csv";


// Store category-specific spending limits.
private static Map<String, Double> categoryBudgets = new LinkedHashMap<>();

    public static void main(String[] args) {

        try {
            expenses = ExpenseStorage.loadExpenses();
            System.out.println("Previous expenses loaded successfully.");
        } catch (IOException e) {
            System.out.println("Could not load expenses: " + e.getMessage());
        }

        loadMonthlyBudget();

// Load saved category budgets.
try {
    categoryBudgets = new LinkedHashMap<>(
        CategoryBudgetStorage.loadBudgets()
    );
    System.out.println("Category budgets loaded successfully.");
} catch (IOException e) {
    System.out.println(
        "Could not load category budgets: " + e.getMessage()
    );
}

int choice = 0;
        while (choice != 21) {
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
            System.out.println("10. Search Expenses");
            System.out.println("11. Sort Expenses");
            System.out.println("12. Export Expenses to CSV");
            System.out.println("13. Smart Spending Insights");
System.out.println("14. Spending Alerts");
System.out.println("15. Budget Performance Report");
System.out.println("16. Expense Statistics Dashboard");
System.out.println("17. Filter Expenses by Date Range");
System.out.println("18. Manage Category Budgets");
System.out.println("19. Monthly Budget Recommendation");
System.out.println("20. Monthly Spending Comparison");
System.out.println("21. Exit");System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println(
                    "Invalid input. Enter a number from 1 to 18."
                );
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
                    setMonthlyBudget();
                    break;

                case 9:
                    viewMonthlyBudget();
                    break;

                case 10:
                    searchExpenses();
                    break;

                case 11:
                    sortExpenses();
                    break;

                case 12:
                    exportExpensesToCSV();
                    break;

                case 13:
                    smartSpendingInsights();
                    break;

             case 14:
    spendingAlerts();
    break;

case 15:
    budgetPerformanceReport();
    break;

case 16:
    expenseStatisticsDashboard();
    break;

case 17:
    filterExpensesByDateRange();
    break;

case 18:
    manageCategoryBudgets();
    break;

case 19:
    recommendMonthlyBudget();
    break;

case 20:
    compareMonthlySpending();
    break;

case 21:
    System.out.println("Exiting Expense Analyzer. Goodbye!");
    break;
default:
    System.out.println(
        "Invalid choice. Please select 1-17."
    );            }
        }

        scanner.close();
    }

    // Analyze spending patterns and provide rule-based recommendations.
    private static void smartSpendingInsights() {

        System.out.println("\n===== SMART SPENDING INSIGHTS =====");

        if (expenses.isEmpty()) {
            System.out.println("No expenses available for analysis.");
            return;
        }

        Map<String, Double> categoryTotals = new LinkedHashMap<>();
        double totalSpending = 0.0;

        for (Expense expense : expenses) {
            String category = expense.getCategory();
            categoryTotals.put(
                category,
                categoryTotals.getOrDefault(category, 0.0)
                    + expense.getAmount()
            );
            totalSpending += expense.getAmount();
        }

        String highestCategory = "";
        double highestAmount = -1.0;

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            if (entry.getValue() > highestAmount) {
                highestAmount = entry.getValue();
                highestCategory = entry.getKey();
            }
        }

        System.out.printf("Total spending: Rs. %.2f%n", totalSpending);
        System.out.println("\nSpending by category:");

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            double percentage = entry.getValue() / totalSpending * 100.0;
            System.out.printf(
                "%s: Rs. %.2f (%.1f%% of total)%n",
                entry.getKey(), entry.getValue(), percentage
            );
        }

        System.out.printf(
            "\nHighest spending category: %s (Rs. %.2f)%n",
            highestCategory, highestAmount
        );

        System.out.println("\n===== RECOMMENDATION =====");
        System.out.println(
            "Your biggest expense category is " + highestCategory + "."
        );
        System.out.println(
            "Review your " + highestCategory
                + " expenses to identify possible savings."
        );

        if (monthlyBudget > 0.0) {
            double currentMonthSpending = getCurrentMonthSpending();
            double budgetPercent = currentMonthSpending / monthlyBudget * 100.0;

            System.out.printf(
                "This month's spending: Rs. %.2f of your Rs. %.2f budget (%.1f%%).%n",
                currentMonthSpending, monthlyBudget, budgetPercent
            );

            if (currentMonthSpending > monthlyBudget) {
                System.out.printf(
                    "Warning: You are over budget by Rs. %.2f.%n",
                    currentMonthSpending - monthlyBudget
                );
                System.out.println(
                    "Recommendation: Reduce non-essential spending for the rest of the month."
                );
            } else if (budgetPercent >= 80.0) {
                System.out.println(
                    "Your budget is nearly used. Consider limiting non-essential purchases."
                );
            } else {
                System.out.println(
                    "Your spending is currently within budget. Keep tracking your expenses."
                );
            }
        } else {
            System.out.println(
                "Set a monthly budget to receive budget-based recommendations."
            );
        }
    }
    // Identify unusually large expenses and provide recommendations.
    private static void spendingAlerts() {

        System.out.println("\n===== SPENDING ALERTS =====");

        if (expenses.isEmpty()) {
            System.out.println("No expenses available to analyze.");
            return;
        }

        final double threshold = 500.0;
        boolean alertFound = false;

        System.out.printf(
            "Alert threshold: Rs. %.2f%n",
            threshold
        );

        for (Expense expense : expenses) {

            if (expense.getAmount() >= threshold) {

                alertFound = true;

                System.out.println(
                    "\nUnusually large expense detected!"
                );

                System.out.println(
                    "Expense: " + expense.getName()
                );

                System.out.println(
                    "Category: " + expense.getCategory()
                );

                System.out.printf(
                    "Amount: Rs. %.2f%n",
                    expense.getAmount()
                );

                System.out.println(
                    "Date: " + expense.getDate()
                );

                System.out.println("Recommendation:");

                System.out.println(
                    "Review this expense to identify possible savings."
                );
            }
        }

        if (!alertFound) {
            System.out.println(
                "No unusually large expenses found. Keep tracking your spending!"
            );
        }
    }


    // Add a new expense.
    private static void addExpense() {

        System.out.print("Enter expense name: ");
        String name = scanner.nextLine().trim();

        if (name.isBlank()) {
            System.out.println("Expense name cannot be empty.");
            return;
        }

        System.out.print(
            "Enter category (Food, Travel, Shopping, etc.): "
        );
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

        expenses.add(new Expense(name, category, amount));
        saveExpenses();

        System.out.println("Expense added successfully!");
        showBudgetWarning();
    }

    // Display all expenses.
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
            System.out.printf(
                "Amount: Rs. %.2f%n",
                expense.getAmount()
            );
            System.out.println("Date: " + expense.getDate());
        }
    }

    // Calculate total spending.
    private static void viewTotalSpending() {

        double total = 0.0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        System.out.printf("Total Spending: Rs. %.2f%n", total);
    }

    // Calculate spending by category.
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

    // Search expenses by name, category, or amount.
private static void searchExpenses() {

    System.out.println("\n===== SEARCH EXPENSES =====");

    if (expenses.isEmpty()) {
        System.out.println("No expenses available to search.");
        return;
    }

    System.out.println("Search by:");
    System.out.println("1. Expense name");
    System.out.println("2. Category");
    System.out.println("3. Exact amount");
    System.out.println("4. Name, category, or amount");
    System.out.print("Choose an option: ");

    String choice = scanner.nextLine().trim();

    if (!choice.matches("[1-4]")) {
        System.out.println("Invalid choice. Select 1, 2, 3, or 4.");
        return;
    }

    System.out.print("Enter your search value: ");
    String query = scanner.nextLine().trim();

    if (query.isBlank()) {
        System.out.println("Search value cannot be empty.");
        return;
    }

    double searchAmount = 0.0;

    if (choice.equals("3")) {
        try {
            searchAmount = Double.parseDouble(query);

            if (!Double.isFinite(searchAmount) || searchAmount < 0) {
                System.out.println("Enter a valid non-negative amount.");
                return;
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid amount.");
            return;
        }
    }

    int matchCount = 0;
    double matchingTotal = 0.0;

    System.out.println("\n===== SEARCH RESULTS =====");

    for (Expense expense : expenses) {

        boolean matches = false;

        if (choice.equals("1")) {
            matches = expense.getName()
                    .toLowerCase()
                    .contains(query.toLowerCase());

        } else if (choice.equals("2")) {
            matches = expense.getCategory()
                    .toLowerCase()
                    .contains(query.toLowerCase());

        } else if (choice.equals("3")) {
            matches = Double.compare(
                    expense.getAmount(),
                    searchAmount
            ) == 0;

        } else if (choice.equals("4")) {
            matches = expense.getName()
                    .toLowerCase()
                    .contains(query.toLowerCase())
                    || expense.getCategory()
                    .toLowerCase()
                    .contains(query.toLowerCase())
                    || String.valueOf(expense.getAmount())
                    .equals(query);
        }

        if (matches) {

            matchCount++;

            System.out.println("\nMatch #" + matchCount);
            System.out.println("Name: " + expense.getName());
            System.out.println("Category: " + expense.getCategory());

            System.out.printf(
                    "Amount: Rs. %.2f%n",
                    expense.getAmount()
            );

            System.out.println("Date: " + expense.getDate());

            matchingTotal += expense.getAmount();
        }
    }

    if (matchCount == 0) {
        System.out.println("No expenses matched your search.");

    } else {

        System.out.println("\n===== SEARCH SUMMARY =====");
        System.out.println("Matching expenses: " + matchCount);

        System.out.printf(
                "Total of matching expenses: Rs. %.2f%n",
                matchingTotal
        );
    }
}
        
// NEW DATE-FILTERING METHOD WILL GO HERE
    // Filter expenses between two dates.
    private static void filterExpensesByDateRange() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses available to filter.");
            return;
        }

        System.out.println("\n===== EXPENSE DATE RANGE FILTER =====");
        System.out.println("Enter dates in yyyy-MM-dd format.");
        System.out.println("Example: 2026-10-01");

        LocalDate startDate;
        LocalDate endDate;

        try {
            System.out.print("Enter start date: ");
            startDate = LocalDate.parse(scanner.nextLine().trim());

            System.out.print("Enter end date: ");
            endDate = LocalDate.parse(scanner.nextLine().trim());
        } catch (java.time.format.DateTimeParseException e) {
            System.out.println("Invalid date! Please use yyyy-MM-dd format.");
            return;
        }

        if (startDate.isAfter(endDate)) {
            System.out.println("Start date cannot be after the end date.");
            return;
        }

        double total = 0;
        int count = 0;

        System.out.println("\n===== FILTERED EXPENSES =====");

        for (Expense expense : expenses) {
            LocalDate expenseDate = expense.getDate();

            if (!expenseDate.isBefore(startDate)
                    && !expenseDate.isAfter(endDate)) {

                System.out.println("------------------------------");
                System.out.println("Name: " + expense.getName());
                System.out.println("Category: " + expense.getCategory());
                System.out.println("Amount: Rs. " + expense.getAmount());
                System.out.println("Date: " + expense.getDate());

                total += expense.getAmount();
                count++;
            }
        }

        if (count == 0) {
            System.out.println("No expenses found within this date range.");
        } else {
            System.out.println("------------------------------");
            System.out.println("Number of expenses: " + count);
            System.out.printf("Total spending: Rs. %.2f%n", total);
        }
    }


        // Sort expenses without changing their saved order.
    private static void sortExpenses() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses available to sort.");
            return;
        }

        System.out.println("\n===== SORT EXPENSES =====");
        System.out.println("1. Amount: Lowest to Highest");
        System.out.println("2. Amount: Highest to Lowest");
        System.out.println("3. Date: Newest First");
        System.out.print("Choose a sorting option: ");

        if (!scanner.hasNextInt()) {
            System.out.println(
                "Invalid input. Please enter 1, 2, or 3."
            );
            scanner.nextLine();
            return;
        }

        int sortChoice = scanner.nextInt();
        scanner.nextLine();

        ArrayList<Expense> sortedExpenses =
            new ArrayList<>(expenses);

        switch (sortChoice) {
            case 1:
                sortedExpenses.sort(
                    Comparator.comparingDouble(Expense::getAmount)
                );
                break;

            case 2:
                sortedExpenses.sort(
                    Comparator.comparingDouble(
                        Expense::getAmount
                    ).reversed()
                );
                break;

            case 3:
                sortedExpenses.sort(
                    Comparator.comparing(
                        Expense::getDate
                    ).reversed()
                );
                break;

            default:
                System.out.println(
                    "Invalid sorting option. Choose 1, 2, or 3."
                );
                return;
        }

        System.out.println("\n===== SORTED EXPENSES =====");

        for (int i = 0; i < sortedExpenses.size(); i++) {
            Expense expense = sortedExpenses.get(i);

            System.out.println("\nExpense #" + (i + 1));
            System.out.println("Name: " + expense.getName());
            System.out.println("Category: " + expense.getCategory());

            System.out.printf(
                "Amount: Rs. %.2f%n",
                expense.getAmount()
            );

            System.out.println("Date: " + expense.getDate());
        }

        System.out.println("\nSorting completed successfully!");
    }

    // Export all expenses to a CSV file.
    private static void exportExpensesToCSV() {

        System.out.println("\n===== EXPORT EXPENSES TO CSV =====");

        if (expenses.isEmpty()) {
            System.out.println("No expenses available to export.");
            return;
        }

        try (PrintWriter writer = new PrintWriter(
                new FileWriter(CSV_FILE))) {

            // Write the column headings.
            writer.println("Name,Category,Amount,Date");

            // Write each expense as a CSV row.
            for (Expense expense : expenses) {

                String name = escapeCSV(expense.getName());
                String category = escapeCSV(expense.getCategory());
                String amount = String.format(
                    java.util.Locale.US,
                    "%.2f",
                    expense.getAmount()
                );
                String date = expense.getDate().toString();

                writer.println(
                    name + ","
                    + category + ","
                    + amount + ","
                    + date
                );
            }

            if (writer.checkError()) {
                System.out.println(
                    "An error occurred while writing the CSV file."
                );
                return;
            }

            System.out.println("Expenses exported successfully!");
            System.out.println("File name: " + CSV_FILE);
            System.out.println(
                "Total expenses exported: " + expenses.size()
            );

            System.out.println(
                "Location: " + new File(CSV_FILE).getAbsolutePath()
            );

        } catch (IOException e) {
            System.out.println(
                "Could not export expenses: " + e.getMessage()
            );
        }
    }

    // Escape CSV text containing commas, quotes, or line breaks.
    private static String escapeCSV(String value) {

        if (value.contains(",")
                || value.contains("\"")
                || value.contains("\n")
                || value.contains("\r")) {

            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }

    // Edit an expense while preserving its date.
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

    // Generate a detailed spending report for a selected month and year.
private static void monthlySpendingReport() {

    System.out.println("\n===== MONTHLY EXPENSE REPORT =====");

    System.out.print("Enter year (e.g. 2026): ");

    if (!scanner.hasNextInt()) {
        System.out.println("Please enter a valid year.");
        scanner.nextLine();
        return;
    }

    int year = scanner.nextInt();
    scanner.nextLine();

    if (year < 1900 || year > 9999) {
        System.out.println("Please enter a year between 1900 and 9999.");
        return;
    }

    System.out.print("Enter month (1 = January, 12 = December): ");

    if (!scanner.hasNextInt()) {
        System.out.println("Please enter a valid month.");
        scanner.nextLine();
        return;
    }

    int month = scanner.nextInt();
    scanner.nextLine();

    if (month < 1 || month > 12) {
        System.out.println("Month must be between 1 and 12.");
        return;
    }

    Map<String, Double> categoryTotals = new LinkedHashMap<>();

    double monthlyTotal = 0.0;
    int expenseCount = 0;

    Expense highestExpense = null;

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
            expenseCount++;

            if (highestExpense == null
                    || expense.getAmount() > highestExpense.getAmount()) {
                highestExpense = expense;
            }
        }
    }

    String monthName = Month.of(month).name();
    monthName = monthName.substring(0, 1)
        + monthName.substring(1).toLowerCase();

    System.out.println("\n===== REPORT SUMMARY =====");
    System.out.println("Month: " + monthName + " " + year);

    if (expenseCount == 0) {
        System.out.println("No expenses recorded for this month.");
        return;
    }

    System.out.printf(
        "Total spent: Rs. %.2f%n",
        monthlyTotal
    );

    System.out.println("Number of expenses: " + expenseCount);

    System.out.printf(
        "Average expense: Rs. %.2f%n",
        monthlyTotal / expenseCount
    );

    System.out.println("\n===== HIGHEST EXPENSE =====");

    System.out.println("Name: " + highestExpense.getName());
    System.out.println("Category: " + highestExpense.getCategory());

    System.out.printf(
        "Amount: Rs. %.2f%n",
        highestExpense.getAmount()
    );

    System.out.println("Date: " + highestExpense.getDate());

    System.out.println("\n===== CATEGORY BREAKDOWN =====");

    for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {

        double percentage =
            entry.getValue() / monthlyTotal * 100.0;

        System.out.printf(
            "%s: Rs. %.2f (%.1f%%)%n",
            entry.getKey(),
            entry.getValue(),
            percentage
        );
    }

    System.out.println("\nMonthly report generated successfully!");
}
// Display overall expense statistics.
private static void expenseStatisticsDashboard() {

    System.out.println("\n===== EXPENSE STATISTICS DASHBOARD =====");

    if (expenses.isEmpty()) {
        System.out.println("No expenses available for analysis.");
        System.out.println("Add some expenses to view your statistics.");
        return;
    }

    double totalSpending = 0.0;

    Expense largestExpense = expenses.get(0);
    Expense smallestExpense = expenses.get(0);

    Map<String, Integer> categoryCounts = new LinkedHashMap<>();

    for (Expense expense : expenses) {

        totalSpending += expense.getAmount();

        if (expense.getAmount() > largestExpense.getAmount()) {
            largestExpense = expense;
        }

        if (expense.getAmount() < smallestExpense.getAmount()) {
            smallestExpense = expense;
        }

        String category = expense.getCategory();

        categoryCounts.put(
            category,
            categoryCounts.getOrDefault(category, 0) + 1
        );
    }

    double averageExpense = totalSpending / expenses.size();

    String mostFrequentCategory = "";
    int highestCategoryCount = 0;

    for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {

        if (entry.getValue() > highestCategoryCount) {
            highestCategoryCount = entry.getValue();
            mostFrequentCategory = entry.getKey();
        }
    }

    System.out.println("\n===== OVERALL SUMMARY =====");
    System.out.println("Total Expense Entries: " + expenses.size());

    System.out.printf(
        "Total Money Spent: Rs. %.2f%n",
        totalSpending
    );

    System.out.printf(
        "Average Expense: Rs. %.2f%n",
        averageExpense
    );

    System.out.println("\n===== LARGEST EXPENSE =====");
    System.out.println("Name: " + largestExpense.getName());
    System.out.println("Category: " + largestExpense.getCategory());

    System.out.printf(
        "Amount: Rs. %.2f%n",
        largestExpense.getAmount()
    );

    System.out.println("\n===== SMALLEST EXPENSE =====");
    System.out.println("Name: " + smallestExpense.getName());
    System.out.println("Category: " + smallestExpense.getCategory());

    System.out.printf(
        "Amount: Rs. %.2f%n",
        smallestExpense.getAmount()
    );

    System.out.println("\n===== MOST FREQUENT CATEGORY =====");
    System.out.println("Category: " + mostFrequentCategory);
    System.out.println("Number of expenses: " + highestCategoryCount);

    System.out.println("\n===== CATEGORY FREQUENCY =====");

    for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
        System.out.printf(
            "%s: %d expense(s)%n",
            entry.getKey(),
            entry.getValue()
        );
    }

    System.out.println("\nExpense statistics generated successfully!");
}
    // Set or update the monthly budget.

    private static void setMonthlyBudget() 
{

        System.out.println("\n===== SET MONTHLY BUDGET =====");
        System.out.print("Enter your monthly budget in Rs.: ");

        if (!scanner.hasNextDouble()) {
            System.out.println("Please enter a valid number.");
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
        saveMonthlyBudget();

        System.out.printf(
            "Monthly budget set to Rs. %.2f%n",
            monthlyBudget
        );

        viewMonthlyBudget();
    }
// Compare the monthly budget with actual spending.
private static void budgetPerformanceReport() {

    System.out.println("\n===== BUDGET PERFORMANCE REPORT =====");

    if (monthlyBudget <= 0) {
        System.out.println("No monthly budget has been set yet.");
        System.out.println("Please select option 8 to set your budget.");
        return;
    }

    double actualSpending = getCurrentMonthSpending();
    double remainingBudget = monthlyBudget - actualSpending;

    double budgetUtilization =
        (actualSpending / monthlyBudget) * 100.0;

    System.out.printf(
        "Monthly Budget:     Rs. %.2f%n",
        monthlyBudget
    );

    System.out.printf(
        "Actual Spending:    Rs. %.2f%n",
        actualSpending
    );

    if (remainingBudget >= 0) {
        System.out.printf(
            "Remaining Budget:   Rs. %.2f%n",
            remainingBudget
        );
    } else {
        System.out.printf(
            "Overspending:       Rs. %.2f%n",
            Math.abs(remainingBudget)
        );
    }

    System.out.printf(
        "Budget Utilization: %.1f%%%n",
        budgetUtilization
    );

    System.out.println("\n===== STATUS =====");

    if (actualSpending > monthlyBudget) {

        System.out.println("ALERT: You have exceeded your budget!");

        System.out.println("\nRECOMMENDATION:");
        System.out.println(
            "Review non-essential expenses and reduce spending."
        );

    } else if (budgetUtilization >= 80.0) {

        System.out.println(
            "WARNING: You have used at least 80% of your budget."
        );

        System.out.println("\nRECOMMENDATION:");
        System.out.println(
            "Limit unnecessary purchases for the rest of the month."
        );

    } else {

        System.out.println(
            "Your spending is currently within your budget."
        );

        System.out.println("\nRECOMMENDATION:");
        System.out.println(
            "Keep tracking your expenses and maintain good spending habits!"
        );
    }

    System.out.println(
        "\nBudget performance report generated successfully!"
    );
}

    // Display budget, spending, and budget status.
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

        if (monthlyTotal > monthlyBudget) {
            System.out.println(
                "\nALERT: You have exceeded your monthly budget!"
            );

            System.out.printf(
                "You spent Rs. %.2f more than your budget.%n",
                monthlyTotal - monthlyBudget
            );

        } else if (percentageUsed >= 80.0) {
            System.out.println(
                "\nWARNING: You have used most of your monthly budget!"
            );

            System.out.printf(
                "Only Rs. %.2f remains in your budget.%n",
                remaining
            );

        } else {
            System.out.println(
                "\nSTATUS: Your spending is under control."
            );
        }
    }

    // Calculate spending for the current month.
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

    // Display a warning after adding or editing an expense.
    private static void showBudgetWarning() {

        if (monthlyBudget <= 0) {
            return;
        }

        double total = getCurrentMonthSpending();
        double percentageUsed = (total / monthlyBudget) * 100.0;

        if (total > monthlyBudget) {
            System.out.println(
                "\nALERT: You have exceeded your monthly budget!"
            );

            System.out.printf(
                "Overspending: Rs. %.2f%n",
                total - monthlyBudget
            );

        } else if (percentageUsed >= 80.0) {
            System.out.println(
                "\nWARNING: You have used most of your monthly budget!"
            );

            System.out.printf(
                "Remaining budget: Rs. %.2f%n",
                monthlyBudget - total
            );
        }
    }

    // Save expenses using ExpenseStorage.
    private static void saveExpenses() {

        try {
            ExpenseStorage.saveExpenses(expenses);

        } catch (IOException e) {
            System.out.println(
                "Could not save expenses: " + e.getMessage()
            );
        }
    }

    // Save the monthly budget.
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

    // Load the saved monthly budget.
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
private static void manageCategoryBudgets() {

    while (true) {

        System.out.println("\n===== CATEGORY BUDGET MANAGEMENT =====");
        System.out.println("1. Set Category Budget");
        System.out.println("2. View Category Budgets");
        System.out.println("3. Return to Main Menu");
        System.out.print("Enter your choice: ");

               String input = scanner.nextLine().trim();

        switch (input) {

            case "1":
                setCategoryBudget();
                break;

            case "2":
                viewCategoryBudgets();
                break;

            case "3":
                return;

            default:
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
        }
    }

}

private static void setCategoryBudget() {

    System.out.print("Enter category name: ");
    String category = scanner.nextLine().trim();

    if (category.isBlank()) {
        System.out.println("Category name cannot be empty.");
        return;
    }

    System.out.print("Enter budget limit (Rs.): ");
    String input = scanner.nextLine().trim();

    try {
        double amount = Double.parseDouble(input);

        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println("Budget must be a positive number.");
            return;
        }

        categoryBudgets.put(category, amount);
        CategoryBudgetStorage.saveBudgets(categoryBudgets);

        System.out.printf(
            "Budget set successfully for %s: Rs. %.2f%n",
            category, amount
        );

    } catch (NumberFormatException e) {
        System.out.println("Invalid amount. Please enter a number.");

    } catch (IOException e) {
        System.out.println(
            "Could not save category budget: " + e.getMessage()
        );
    }
}
private static void viewCategoryBudgets() {

    System.out.println("\n===== CATEGORY BUDGETS =====");

    if (categoryBudgets.isEmpty()) {
        System.out.println("No category budgets have been set yet.");
        return;
    }

    for (Map.Entry<String, Double> entry : categoryBudgets.entrySet()) {

        String category = entry.getKey();
        double limit = entry.getValue();
        double spent = 0.0;

        for (Expense expense : expenses) {
            if (expense.getCategory().equalsIgnoreCase(category)) {
                spent += expense.getAmount();
            }
        }

        System.out.println("\nCategory: " + category);
        System.out.printf("Budget limit: Rs. %.2f%n", limit);
        System.out.printf("Total spent:  Rs. %.2f%n", spent);
        System.out.printf("Remaining:    Rs. %.2f%n", limit - spent);

               if (spent > limit) {
            System.out.println("WARNING: Category budget exceeded!");
        } else {
            System.out.println("Status: Within category budget.");
        }
    }
}

    private static void recommendMonthlyBudget() {
    System.out.println("\n===== MONTHLY BUDGET RECOMMENDATION =====");

    if (expenses.isEmpty()) {
        System.out.println("No expenses available to analyze.");
        return;
    }

    Map<String, Double> monthlyTotals = new LinkedHashMap<>();

    for (Expense expense : expenses) {

        String monthKey = expense.getDate().getYear()
                + "-"
                + String.format("%02d", expense.getDate().getMonthValue());

        double currentTotal = monthlyTotals.getOrDefault(monthKey, 0.0);

        monthlyTotals.put(
            monthKey,
            currentTotal + expense.getAmount()
        );
    }

    double totalSpending = 0.0;

    for (double monthlyTotal : monthlyTotals.values()) {
        totalSpending += monthlyTotal;
    }

    double averageMonthlySpending =
            totalSpending / monthlyTotals.size();

    double recommendedBudget = averageMonthlySpending * 1.10;

    System.out.printf(
        "Months analyzed: %d%n",
        monthlyTotals.size()
    );

    System.out.printf(
        "Average monthly spending: Rs. %.2f%n",
        averageMonthlySpending
    );

    System.out.printf(
        "Recommended monthly budget (10%% buffer): Rs. %.2f%n",
        recommendedBudget
    );

    System.out.println(
        "\nRecommendation: Use this amount as a starting point for your monthly budget."
    );
}
private static void compareMonthlySpending() {

    System.out.println("\n===== MONTHLY SPENDING COMPARISON =====");

    if (expenses.isEmpty()) {
        System.out.println("No expenses available to analyze.");
        return;
    }

    Scanner input = new Scanner(System.in);

    System.out.print("Enter the first month (1-12): ");
    int firstMonth = input.nextInt();

    System.out.print("Enter the first year (e.g., 2026): ");
    int firstYear = input.nextInt();

    System.out.print("Enter the second month (1-12): ");
    int secondMonth = input.nextInt();

    System.out.print("Enter the second year (e.g., 2026): ");
    int secondYear = input.nextInt();

    if (firstMonth < 1 || firstMonth > 12
            || secondMonth < 1 || secondMonth > 12
            || firstYear < 1 || secondYear < 1) {

        System.out.println("Invalid month or year.");
        return;
    }

    double firstTotal = 0.0;
    double secondTotal = 0.0;

    for (Expense expense : expenses) {

        int month = expense.getDate().getMonthValue();
        int year = expense.getDate().getYear();

        if (month == firstMonth && year == firstYear) {
            firstTotal += expense.getAmount();
        }

        if (month == secondMonth && year == secondYear) {
            secondTotal += expense.getAmount();
        }
    }

    System.out.printf(
        "%nFirst month spending:  Rs. %.2f%n",
        firstTotal
    );

    System.out.printf(
        "Second month spending: Rs. %.2f%n",
        secondTotal
    );

    double difference = secondTotal - firstTotal;

    System.out.printf(
        "Spending difference:   Rs. %.2f%n",
        Math.abs(difference)
    );

    if (firstTotal == 0.0 && secondTotal == 0.0) {

        System.out.println("No spending recorded for either month.");

    } else if (firstTotal == 0.0) {

        System.out.println(
            "Percentage change cannot be calculated because the first month has no spending."
        );

    } else {

        double percentageChange =
                (difference / firstTotal) * 100.0;

        System.out.printf(
            "Spending change: %.2f%%%n",
            percentageChange
        );

        if (difference > 0) {
            System.out.println("Your spending increased.");
        } else if (difference < 0) {
            System.out.println("Great! Your spending decreased.");
        } else {
            System.out.println("Your spending stayed the same.");
        }
    }
}
}
