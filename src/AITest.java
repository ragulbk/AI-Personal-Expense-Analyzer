import java.util.ArrayList;

public class AITest {

    public static void main(String[] args) {

        AIExpenseAssistant assistant = new AIExpenseAssistant();

        if (!assistant.isAIConfigured()) {
            System.out.println("API key is missing.");
            return;
        }

        try {

            ArrayList<Expense> expenses =
                    ExpenseStorage.loadExpenses();

            System.out.println("\nAnalyzing your expenses using Gemini AI...");

            String response = assistant.analyzeExpenses(expenses);

            System.out.println("\n========== AI EXPENSE ANALYSIS ==========");
            System.out.println(response);
            System.out.println("=========================================");

        } catch (Exception e) {

            System.out.println("Error loading expenses: " + e.getMessage());

        }
    }
}