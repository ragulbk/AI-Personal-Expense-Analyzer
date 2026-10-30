import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
public class AIExpenseAssistant {
private static final String API_URL =
    "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.8-flash:generateContent";
    private final HttpClient httpClient;

    public AIExpenseAssistant() {

        httpClient = HttpClient.newHttpClient();

        System.out.println("AI Expense Assistant initialized.");
    }
    public boolean isAIConfigured() {

        String apiKey = System.getenv("AI_API_KEY");

        return apiKey != null && !apiKey.isBlank();
    }

    // Send a prompt to Gemini AI.
    public String askGemini(String prompt) {

        String apiKey = System.getenv("AI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            return "Error: API key is missing.";
        }

        try {

            String requestBody = """
                {
                    "contents": [
                        {
                            "parts": [
                                {
                                    "text": "%s"
                                }
                            ]
                        }
                    ]
                }
                """.formatted(prompt.replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n"));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
if (response.statusCode() == 200) {
    return extractGeminiText(response.body());
}
            return "Gemini API Error: " + response.body();

        } catch (Exception e) {

            return "Connection error: " + e.getMessage();

        }
}
        // Extract readable text from Gemini's JSON response.
    private String extractGeminiText(String json) {

        String searchText = "\"text\"";

        int keyPosition = json.indexOf(searchText);

        if (keyPosition == -1) {
            return "No AI response text found.";
        }

        int colonPosition = json.indexOf(":", keyPosition);
        int startPosition = json.indexOf("\"", colonPosition + 1);

        if (startPosition == -1) {
            return "Unable to read AI response.";
        }

        StringBuilder result = new StringBuilder();

        for (int i = startPosition + 1; i < json.length(); i++) {

            char current = json.charAt(i);

            if (current == '\\' && i + 1 < json.length()) {

                char next = json.charAt(++i);

                switch (next) {
                    case 'n' -> result.append('\n');
                    case 'r' -> result.append('\r');
                    case 't' -> result.append('\t');
                    case '"' -> result.append('"');
                    case '\\' -> result.append('\\');
                    case '/' -> result.append('/');
                    default -> result.append(next);
                }

            } else if (current == '"') {

                return result.toString();

            } else {

                result.append(current);
            }
        }

               return "Unable to read AI response.";
    }

    // Analyze the user's actual expenses using Gemini AI.
    public String analyzeExpenses(ArrayList<Expense> expenses) {

        if (expenses == null || expenses.isEmpty()) {
            return "No expenses available for AI analysis.";
        }

        StringBuilder expenseDetails = new StringBuilder();

        for (Expense expense : expenses) {

            expenseDetails.append("Name: ")
                    .append(expense.getName())
                    .append(", Category: ")
                    .append(expense.getCategory())
                    .append(", Amount: Rs. ")
                    .append(expense.getAmount())
                    .append(", Date: ")
                    .append(expense.getDate())
                    .append("\n");
        }

        String prompt = """
            You are a personal finance assistant.

            Analyze the following expense records.

            Please provide:
            1. Total spending.
            2. Highest spending category.
            3. Important spending patterns.
            4. Suggestions to reduce unnecessary expenses.
            5. Three practical money-saving tips.

            Explain your findings in simple language.

            Expense Records:
            %s
            """.formatted(expenseDetails);

        return askGemini(prompt);
    }
}
