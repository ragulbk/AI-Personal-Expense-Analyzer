import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Store multiple expenses
        ArrayList<Expense> expenses = new ArrayList<>();

        System.out.println("================================");
        System.out.println("  PERSONAL EXPENSE ANALYZER");
        System.out.println("================================");

        System.out.print("How many expenses do you want to add? ");
        int count = scanner.nextInt();
        scanner.nextLine();

        // Get expense details
        for (int i = 0; i < count; i++) {

            System.out.println("\nExpense " + (i + 1));

            System.out.print("Enter expense name: ");
            String name = scanner.nextLine();

            System.out.print("Enter category: ");
            String category = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            // Create an Expense object
            Expense expense = new Expense(name, category, amount);

            // Add it to the list
            expenses.add(expense);

            System.out.println("Expense added successfully!");
        }

        // Calculate total spending
        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        // Display all expenses
        System.out.println("\n========== YOUR EXPENSES ==========");

        for (Expense expense : expenses) {

            System.out.println(
                expense.getName()
                + " | "
                + expense.getCategory()
                + " | Rs. "
                + expense.getAmount()
            );
        }

        System.out.println("-----------------------------------");
        System.out.printf("Total Spending: Rs. %.2f%n", total);

        scanner.close();
    }
}