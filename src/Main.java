import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        ArrayList<String> expenseNames = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<Double> amounts = new ArrayList<>();

        System.out.println("================================");
        System.out.println("  PERSONAL EXPENSE ANALYZER");
        System.out.println("================================");

        System.out.print("How many expenses do you want to enter? ");
        int count = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < count; i++) {

            System.out.println("\nExpense " + (i + 1));

            System.out.print("Enter expense name: ");
            String name = scanner.nextLine();

            System.out.print("Enter category: ");
            String category = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            expenseNames.add(name);
            categories.add(category);
            amounts.add(amount);

            System.out.println("Expense added successfully!");
        }

        System.out.println("\n========== EXPENSE LIST ==========");

        for (int i = 0; i < expenseNames.size(); i++) {

            System.out.println("\nExpense " + (i + 1));
            System.out.println("Name: " + expenseNames.get(i));
            System.out.println("Category: " + categories.get(i));
            System.out.printf("Amount: Rs. %.2f%n", amounts.get(i));
        }

        scanner.close();
    }
}