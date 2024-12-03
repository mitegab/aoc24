import java.io.*;
import java.util.regex.*;

public class Day3Part2 {
    public static void main(String[] args) {
        String inputFileName = "input.txt";
        try {

            StringBuilder input = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    input.append(line);
                }
            }


            Pattern mulPattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
            Pattern doPattern = Pattern.compile("do\\(\\)");
            Pattern dontPattern = Pattern.compile("don't\\(\\)");

            Matcher mulMatcher = mulPattern.matcher(input.toString());
            Matcher doMatcher = doPattern.matcher(input.toString());
            Matcher dontMatcher = dontPattern.matcher(input.toString());

            int totalSum = 0;
            boolean isEnabled = true;


            Pattern allPatterns = Pattern.compile("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)");
            Matcher matcher = allPatterns.matcher(input.toString());


            while (matcher.find()) {
                String match = matcher.group();

                if (match.startsWith("do()")) {
                    isEnabled = true;
                } else if (match.startsWith("don't()")) {
                    isEnabled = false;
                } else if (match.startsWith("mul(") && isEnabled) {

                    int x = Integer.parseInt(matcher.group(1));
                    int y = Integer.parseInt(matcher.group(2));
                    totalSum += x * y;
                }
            }


            System.out.println("Total sum of enabled mul instructions: " + totalSum);

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }
}
