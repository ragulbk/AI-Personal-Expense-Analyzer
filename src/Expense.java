import java.time.LocalDate;

public class Expense {

    private String name;
    private String category;
    private double amount;
    private LocalDate date;

    // Constructor to create an expense with today's date.
    public Expense(String name, String category, double amount) {
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.date = LocalDate.now();
    }

    // Constructor to create an expense with a specific date.
    public Expense(String name, String category, double amount,
                   LocalDate date) {
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    // Returns the expense name.
    public String getName() {
        return name;
    }

    // Returns the expense category.
    public String getCategory() {
        return category;
    }

    // Returns the expense amount.
    public double getAmount() {
        return amount;
    }

    // Returns the expense date.
    public LocalDate getDate() {
        return date;
    }
}