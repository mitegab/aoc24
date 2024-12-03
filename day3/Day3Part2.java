import java.io.*;
import java.util.regex.*;

public class Day3Part2 {
    public static void main(String[] args) {
        String inputFileName = "input.txt";
        try {
            // Read the input file
            StringBuilder input = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    input.append(line);
                }
            }

            // Regex patterns for mul, do, and don't instructions
            Pattern mulPattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
            Pattern doPattern = Pattern.compile("do\\(\\)");
            Pattern dontPattern = Pattern.compile("don't\\(\\)");

            Matcher mulMatcher = mulPattern.matcher(input.toString());
            Matcher doMatcher = doPattern.matcher(input.toString());
            Matcher dontMatcher = dontPattern.matcher(input.toString());

            int totalSum = 0;
            boolean isEnabled = true; // At the beginning, mul instructions are enabled

            // Combine all matches into a single list of events
            Pattern allPatterns = Pattern.compile("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)");
            Matcher matcher = allPatterns.matcher(input.toString());

            // Process all matches in order of appearance
            while (matcher.find()) {
                String match = matcher.group();

                if (match.startsWith("do()")) {
                    isEnabled = true; // Enable future mul instructions
                } else if (match.startsWith("don't()")) {
                    isEnabled = false; // Disable future mul instructions
                } else if (match.startsWith("mul(") && isEnabled) {
                    // Process enabled mul instructions
                    int x = Integer.parseInt(matcher.group(1));
                    int y = Integer.parseInt(matcher.group(2));
                    totalSum += x * y;
                }
            }

            // Print the result
            System.out.println("Total sum of enabled mul instructions: " + totalSum);

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }
}
