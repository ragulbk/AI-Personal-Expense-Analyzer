import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("================================");
        System.out.println("  PERSONAL EXPENSE ANALYZER");
        System.out.println("================================");

        System.out.print("Enter expense name: ");
        String name = scanner.nextLine();

        System.out.print("Enter expense category: ");
        String category = scanner.nextLine();

        System.out.print("Enter expense amount: ");
        double amount = scanner.nextDouble();

        System.out.println("\nExpense added successfully!");
        System.out.println("----------------------------");
        System.out.println("Name: " + name);
        System.out.println("Category: " + category);
        System.out.printf("Amount: Rs. %.2f%n", amount);

        scanner.close();
    }
}